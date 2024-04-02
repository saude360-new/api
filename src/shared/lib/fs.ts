import fs from 'node:fs';


/**
 * Ensures that the directory exists. If the directory structure does not exist, it is created.
 * @param dirname 
 * @returns 
 */
export async function ensureDir(dirname: fs.PathLike): Promise<void> {
  if(!fs.existsSync(dirname)) return new Promise((resolve, reject) => {
    fs.mkdir(dirname, { recursive: true, mode: 0o755 }, (err) => {
      if(err) return reject(err);
      resolve();
    });
  });

  const stats = await fs.promises.stat(dirname);

  if(!stats.isDirectory()) {
    await fs.promises.mkdir(dirname, { recursive: true, mode: 0o755 });
  }
}

/**
 * Ensures that the directory exists synchronous. If the directory structure does not exist, it is created.
 * @param dirname 
 * @returns 
 */
export function ensureDirSync(dirname: fs.PathLike): void {
  if(!fs.existsSync(dirname)) return void(fs.mkdirSync(dirname, { recursive: true, mode: 0o755 }));
    
  const stats = fs.statSync(dirname);
    
  if(!stats.isDirectory()) {
    fs.mkdirSync(dirname, { recursive: true, mode: 0o755 });
  }
}


export function getMimeType(file: Buffer): string | null {
  const magicNumbers = {
    'image/jpeg': [0xFF, 0xD8],
    'image/png': [0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A],
    'image/gif': [0x47, 0x49, 0x46],
    'image/webp': [0x52, 0x49, 0x46, 0x46],
    'image/flif': [0x46, 0x4C, 0x49, 0x46],
    'image/x-xcf': [0x67, 0x49, 0x54, 0x38, 0x36, 0x2D, 0x35, 0x2E],
    'image/x-canon-cr2': [0x49, 0x49, 0x2A, 0x00],
    'image/x-canon-cr3': [0x49, 0x49, 0xBC],
    'image/tiff': [0x49, 0x49, 0x2A, 0x00],
    'image/bmp': [0x42, 0x4D],
    'image/icns': [0x69, 0x63, 0x6E, 0x73],
    'image/vnd.ms-photo': [0x49, 0x49, 0xBC],
    'image/vnd.adobe.photoshop': [0x38, 0x42, 0x50, 0x53],
    'application/x-indesign': [0x06, 0x06, 0xED, 0xF5],
    'application/epub+zip': [0x50, 0x4B, 0x03, 0x04],
    'application/x-xpinstall': [0x50, 0x4B, 0x03, 0x04],
    'application/vnd.oasis.opendocument.text': [0x50, 0x4B, 0x03, 0x04],
    'application/vnd.oasis.opendocument.spreadsheet': [0x50, 0x4B, 0x03, 0x04],
    'application/vnd.oasis.opendocument.presentation': [0x50, 0x4B, 0x03, 0x04],
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document': [0x50, 0x4B, 0x03, 0x04],
    'application/vnd.openxmlformats-officedocument.presentationml.presentation': [0x50, 0x4B, 0x03, 0x04],
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': [0x50, 0x4B, 0x03, 0x04],
    'application/zip': [0x50, 0x4B, 0x03, 0x04],
    'application/x-tar': [0x75, 0x73, 0x74, 0x61, 0x72],
    'application/x-rar-compressed': [0x52, 0x61, 0x72, 0x21, 0x1A, 0x07, 0x00],
    'application/gzip': [0x1F, 0x8B],
    'application/x-bzip2': [0x42, 0x5A, 0x68],
    'application/x-7z-compressed': [0x37, 0x7A, 0xBC, 0xAF, 0x27, 0x1C],
    'application/x-apple-diskimage': [0x78, 0x61, 0x72, 0x21],
    'video/mp4': [0x66, 0x74, 0x79, 0x70, 0x4D, 0x53, 0x4E, 0x56],
    'audio/midi': [0x4D, 0x54, 0x68, 0x64],
    'video/x-matroska': [0x1A, 0x45, 0xDF, 0xA3],
    'video/webm': [0x1A, 0x45, 0xDF, 0xA3],
    'video/quicktime': [0x00, 0x00, 0x00, 0x18, 0x66, 0x74, 0x79, 0x70],
    'video/vnd.avi': [0x52, 0x49, 0x46, 0x46],
    'audio/wav': [0x52, 0x49, 0x46, 0x46],
    'audio/qcelp': [0x52, 0x49, 0x46, 0x46],
    'audio/x-ms-asf': [0x30, 0x26, 0xB2, 0x75],
    'video/x-ms-asf': [0x30, 0x26, 0xB2, 0x75],
    'application/vnd.ms-asf': [0x30, 0x26, 0xB2, 0x75],
    'video/mpeg': [0x00, 0x00, 0x01, 0xBA],
    'video/3gpp': [0x00, 0x00, 0x00, 0x20, 0x66, 0x74, 0x79, 0x70],
    'audio/mpeg': [0xFF, 0xFB],
    'audio/mp4': [0x66, 0x74, 0x79, 0x70, 0x33, 0x67, 0x70, 0x34],
    'audio/opus': [0x4F, 0x70, 0x75, 0x73],
    'video/ogg': [0x4F, 0x67, 0x67, 0x53],
    'audio/ogg': [0x4F, 0x67, 0x67, 0x53],
    'application/ogg': [0x4F, 0x67, 0x67, 0x53],
    'audio/x-flac': [0x66, 0x4C, 0x61, 0x43],
    'audio/ape': [0x4D, 0x41, 0x43, 0x20],
    'audio/wavpack': [0x77, 0x76, 0x70, 0x6B],
    'audio/amr': [0x23, 0x21, 0x41, 0x4D, 0x52],
    'application/pdf': [0x25, 0x50, 0x44, 0x46],
    'application/x-elf': [0x7F, 0x45, 0x4C, 0x46],
    'application/x-mach-binary': [0xFE, 0xED, 0xFA, 0xCE],
    'application/x-msdownload': [0x4D, 0x5A],
    'application/x-shockwave-flash': [0x46, 0x57, 0x53],
    'application/rtf': [0x7B, 0x5C, 0x72, 0x74, 0x66],
    'application/wasm': [0x00, 0x61, 0x73, 0x6D],
    'font/woff': [0x77, 0x4F, 0x46, 0x46],
    'font/woff2': [0x77, 0x4F, 0x46, 0x32],
    'application/vnd.ms-fontobject': [0x00, 0x01, 0x00, 0x00],
    'font/ttf': [0x00, 0x01, 0x00, 0x00],
    'font/otf': [0x4F, 0x54, 0x54, 0x4F],
    'image/x-icon': [0x00, 0x00, 0x01, 0x00],
    'video/x-flv': [0x46, 0x4C, 0x56, 0x01],
    'application/postscript': [0x25, 0x21],
    'application/eps': [0x25, 0x21],
    'application/x-xz': [0xFD, 0x37, 0x7A, 0x58, 0x5A, 0x00],
    'application/x-sqlite3': [0x53, 0x51, 0x4C, 0x69, 0x74, 0x65, 0x20, 0x66],
    'application/x-nintendo-nes-rom': [0x4E, 0x45, 0x53, 0x1A],
    'application/x-google-chrome-extension': [0x43, 0x72, 0x32, 0x34],
    'application/vnd.ms-cab-compressed': [0x4D, 0x53, 0x43, 0x46],
    'application/x-deb': [0x21, 0x3C, 0x61, 0x72, 0x63, 0x68, 0x3E],
    'application/x-unix-archive': [0x1F, 0x9D],
    'application/x-rpm': [0xED, 0xAB, 0xEE, 0xDB],
    'application/x-compress': [0x1F, 0x9D],
    'application/x-lzip': [0x4C, 0x5A, 0x49, 0x50],
    'application/x-cfb': [0x50, 0x4B, 0x03, 0x04],
    'application/x-mie': [0x4D, 0x49, 0x45],
    'application/x-apache-arrow': [0x41, 0x72, 0x72, 0x4F, 0x77],
    'application/mxf': [0x06, 0x0E, 0x2B, 0x34],
    'video/mp2t': [0x47, 0x00, 0x00, 0x00, 0x01],
    'application/x-blender': [0x42, 0x4C, 0x45, 0x4E, 0x44, 0x45, 0x52],
    'image/bpg': [0x42, 0x50, 0x47, 0xFB],
    'image/j2c': [0xFF, 0x4F, 0xFF, 0x51],
    'image/jp2': [0x00, 0x00, 0x00, 0x0C, 0x6A, 0x50, 0x20, 0x20],
    'image/jpx': [0x0D, 0x0A, 0x87, 0x0A],
    'image/jpm': [0x6A, 0x50, 0x20, 0x20],
    'image/mj2': [0x6D, 0x6A, 0x70, 0x32],
    'audio/aiff': [0x46, 0x4F, 0x52, 0x4D],
    'application/xml': [0x3C, 0x3F, 0x78, 0x6D],
    'application/x-mobipocket-ebook': [0x4D, 0x4F, 0x42, 0x49],
    'image/heif': [0x66, 0x74, 0x79, 0x70, 0x68, 0x65, 0x69, 0x66],
    'image/heif-sequence': [0x66, 0x74, 0x79, 0x70, 0x68, 0x65, 0x69, 0x66],
    'image/heic': [0x66, 0x74, 0x79, 0x70, 0x68, 0x65, 0x69, 0x63],
    'image/heic-sequence': [0x66, 0x74, 0x79, 0x70, 0x68, 0x65, 0x69, 0x63],
    'image/ktx': [0xAB, 0x4B, 0x54, 0x58],
    'application/dicom': [0x44, 0x49, 0x43, 0x4D],
    'audio/x-musepack': [0x4D, 0x50, 0x2B],
    'text/calendar': [0x42, 0x45, 0x47, 0x49, 0x4E],
    'image/svg+xml': [0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20],
  };


  for(const [type, numbers] of Object.entries(magicNumbers)) {
    if(file.length >= numbers.length) {
      const match = numbers.every((num, index) => num === file[index]);
      if(match) return type;
    }
  }

  return null;
}
