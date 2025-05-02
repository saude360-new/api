-- Stage A: SCHEMA DEFINITION

CREATE TYPE user_role AS ENUM('caregiver', 'patient');

CREATE TABLE IF NOT EXISTS users (
  user_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  birth_date DATE NOT NULL,
  gender VARCHAR(8) NOT NULL CHECK (gender IN ('male', 'female')),
  email_address TEXT NOT NULL UNIQUE,
  email_hash TEXT NOT NULL UNIQUE,
  password_digest TEXT NOT NULL,
  salt TEXT NOT NULL UNIQUE,
  role user_role NOT NULL DEFAULT 'caregiver'::user_role,
  symmetric_key TEXT NOT NULL UNIQUE,
  public_key TEXT NOT NULL UNIQUE,
  private_key TEXT NOT NULL UNIQUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_metadata (
  user_metadata_row_id SERIAL NOT NULL PRIMARY KEY,
  user_id VARCHAR(128) NOT NULL,
  metadata_key TEXT NOT NULL,
  metadata_value TEXT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT
);


CREATE TYPE device_status AS ENUM('pairing', 'connected', 'disconnected');

CREATE TABLE IF NOT EXISTS devices (
  device_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  bluetooth_chip_uid VARCHAR(128) NOT NULL UNIQUE,
  serial_number VARCHAR(128) NOT NULL UNIQUE,
  bluetooth_version VARCHAR(12) NOT NULL,
  last_connected TIMESTAMP WITHOUT TIME ZONE NULL,
  rssi INT NOT NULL DEFAULT 0,
  status device_status NOT NULL DEFAULT 'disconnected'::device_status,
  pairing_key TEXT NOT NULL UNIQUE,
  firmware_version VARCHAR(128) NOT NULL,
  last_error TEXT NULL,
  connection_error TEXT NULL,
  registered_by VARCHAR(128) NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (registered_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS data_exchange_logs (
  log_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  device_id VARCHAR(128) NOT NULL,
  data_sent TEXT NULL,
  data_received TEXT NULL,
  transport_props JSON NOT NULL DEFAULT '{}'::JSON,
  sequence SERIAL NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS device_data (
  payload_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  device_id VARCHAR(128) NOT NULL,
  sequence SERIAL NOT NULL,
  time_serie VARCHAR(8) NOT NULL CHECK (time_serie IN ('day', 'week', 'month', 'year')),
  oximetry_graph JSON NOT NULL,
  temp_graph JSON NOT NULL,
  acceleration_graph JSON NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE ON UPDATE RESTRICT
);


CREATE TABLE IF NOT EXISTS accounts_relationship (
  accounts_relationship_row_id SERIAL NOT NULL PRIMARY KEY,
  patient_id VARCHAR(128) NOT NULL,
  caregiver_id VARCHAR(128) NOT NULL,

  FOREIGN KEY (patient_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (caregiver_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT
  );

-- End Stage A

-- Stage B: CREATE VIEWS

CREATE VIEW user_with_metadata AS SELECT
  u.*,
  json_agg(
    json_build_object(
      'metadata_key', um.metadata_key,
      'metadata_value', um.metadata_value
    )
  )
FROM
  users u
JOIN
  user_metadata um ON u.user_id = um.user_id
GROUP BY
  u.user_id,
  um.user_metadata_row_id;


CREATE VIEW device_with_users AS SELECT
  d.device_id,
  d.serial_number,
  d.status,
  u.user_id AS registered_by,
  u.first_name,
  u.last_name
FROM
  devices d
LEFT JOIN
  users u ON u.user_id = d.registered_by
GROUP BY 
  u.user_id,
  d.device_id;


CREATE VIEW data_exchange_summary AS SELECT
  d.device_id,
  d.serial_number,
  COUNT(l.log_id) AS log_count,
  MAX(l.created_at) AS last_log_time
FROM
  devices d
LEFT JOIN
  data_exchange_logs l ON l.device_id = d.device_id
GROUP BY
  d.device_id,
  l.log_id,
  d.serial_number;


CREATE VIEW patient_caregiver_relationships AS SELECT
  ar.patient_id,
  ar.caregiver_id,
  ar.device_id,
  CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
  CONCAT(c.first_name, ' ', c.last_name) AS caregiver_name
FROM
  accounts_relationship ar
JOIN
  users p ON ar.patient_id = p.user_id
JOIN
  users c ON ar.caregiver_id = c.user_id;

-- End Stage B

-- Stage C: CREATE INDEXES

CREATE INDEX idx_users_email_id ON users(user_id, email_hash);
CREATE INDEX idx_users_email_id_rev ON users(email_hash, user_id);

CREATE INDEX idx_users_email ON users(email_hash);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_user_metadata_user_id ON user_metadata(user_id);

CREATE INDEX idx_devices_registered_by ON devices(registered_by);
CREATE INDEX idx_data_exchange_logs_device_id ON data_exchange_logs(device_id);
CREATE INDEX idx_device_data_device_id ON device_data(device_id);

CREATE INDEX idx_accounts_relationship_patient_id ON accounts_relationship(patient_id);
CREATE INDEX idx_accounts_relationship_caregiver_id ON accounts_relationship(caregiver_id);
CREATE INDEX idx_accounts_relationship_device_id ON accounts_relationship(device_id);

-- End Stage C

-- Stage D: CREATE TRIGGERS

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER
  update_users_timestamp
BEFORE
  UPDATE ON users
FOR EACH ROW
  EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER
  update_devices_timestamp
BEFORE
  UPDATE ON devices
FOR EACH ROW
  EXECUTE FUNCTION update_timestamp();

-- End Stage D
