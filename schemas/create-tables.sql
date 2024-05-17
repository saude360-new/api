CREATE TYPE user_role AS ENUM('caregiver', 'patient');

CREATE TABLE IF NOT EXISTS users (
  user_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  role user_role NOT NULL DEFAULT 'caregiver'::user_role,
  birth_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  gender CHAR(1) NULL,
  email_address TEXT NOT NULL UNIQUE,
  email_hash TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL UNIQUE,
  salt TEXT NOT NULL UNIQUE,
  symmetric_key TEXT NOT NULL UNIQUE,
  public_key TEXT NOT NULL UNIQUE,
  private_key TEXT NOT NULL UNIQUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_metadata (
  user_metadata_row_id SERIAL PRIMARY KEY NOT NULL,
  user_id VARCHAR(128) NOT NULL,
  metadata_key TEXT NOT NULL,
  metadata_value TEXT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS devices (
  device_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  user_id VARCHAR(128) NOT NULL,
  sn TEXT NOT NULL UNIQUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS patient_caregiver_relationships (
  patient_caregiver_relationships_row_id SERIAL PRIMARY KEY NOT NULL,
  patient_id VARCHAR(128) NOT NULL,
  caregiver_id VARCHAR(128) NOT NULL,
  patient_device_id VARCHAR(128) NOT NULL,
  FOREIGN KEY (patient_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (caregiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (patient_device_id) REFERENCES devices(device_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS sessions (
  session_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  user_id VARCHAR(128) NULL,
  headers JSON NOT NULL DEFAULT '{}'::JSON,
  payload_hash TEXT NOT NULL,
  payload_merkle_root TEXT NOT NULL,
  signature TEXT NOT NULL,
  ip_address TEXT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS session_payload_chunks (
  session_payload_chunks_row_id SERIAL NOT NULL PRIMARY KEY,
  session_id VARCHAR(128) NOT NULL,
  chunk_index BIGINT NOT NULL,
  chunk_hash TEXT NOT NULL,
  chunk_value TEXT NOT NULL,
  FOREIGN KEY (session_id) REFERENCES sessions(session_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
  token_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  expires_at VARCHAR(64) NOT NULL,
  user_id VARCHAR(128) NOT NULL,
  created_at VARCHAR(64) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS device_data (
  record_Id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  device_id VARCHAR(128) NOT NULL,
  heart_rate INTEGER NOT NULL,
  oximetry INTEGER NOT NULL,
  temperature DECIMAL NOT NULL,
  x_axis_acceleration DECIMAL NOT NULL,
  y_axis_acceleration DECIMAL NOT NULL,
  z_axis_acceleration DECIMAL NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS medications (
  medication_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  medication_name TEXT NOT NULL,
  medication_description TEXT NOT NULL,
  image_url TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS medications_pannel (
  record_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  medication_id VARCHAR(128) NOT NULL,
  user_id VARCHAR(128) NOT NULL,
  dosage DECIMAL NOT NULL,
  dosage_unit VARCHAR(16) NOT NULL,
  frequency VARCHAR(32) NOT NULL,
  recommendation TEXT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (medication_id) REFERENCES medications(medication_id) ON DELETE SET NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TYPE message_content_type AS ENUM('text', 'image');

CREATE TABLE IF NOT EXISTS messages (
  message_id VARCHAR(128) NOT NULL UNIQUE PRIMARY KEY,
  incoming_id VARCHAR(128) NOT NULL,
  outgoing_id VARCHAR(128) NOT NULL,
  content TEXT NOT NULL,
  content_type message_content_type NOT NULL DEFAULT 'text'::message_content_type,
  content_mimetype TEXT NOT NULL DEFAULT 'text/plain',
  reply_to VARCHAR(128) NULL,
  sent_at TIMESTAMP WITH TIME ZONE NOT NULL,
  readed_at TIMESTAMP WITH TIME ZONE NULL,
  FOREIGN KEY (incoming_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (outgoing_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (reply_to) REFERENCES messages(message_id) ON DELETE CASCADE
);