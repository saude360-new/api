
/* eslint-disable @typescript-eslint/no-var-requires */
const fs = require('node:fs');
const path = require('node:path');


async function recursiveDeleteDirectoryAndSubcontents(dir) {
  if(!fs.existsSync(dir)) return;

  for(const item of (await fs.promises.readdir(dir))) {
    const current = path.join(dir, item);
    const stat = await fs.promises.stat(current);

    if(stat.isDirectory()) {
      await recursiveDeleteDirectoryAndSubcontents(current);
    } else {
      await fs.promises.unlink(current);
    }
  }

  await fs.promises.rmdir(dir);
}


async function removeUneccesaryFiles(dir) {
  for(const item of (await fs.promises.readdir(dir))) {
    const current = path.join(dir, item);
    const stat = await fs.promises.stat(current);

    if(stat.isDirectory()) {
      await removeUneccesaryFiles(current);
    } else if(item.endsWith('.spec.js') || item.endsWith('.d.js')) {
      await fs.promises.unlink(current);
    }
  }
}


async function main() {
  await removeUneccesaryFiles(path.join(process.cwd(), 'dist'));
}

main().catch(console.error);

