File buildLogFile = new File( basedir, "build.log" );
assert buildLogFile.exists();

String buildLog = buildLogFile.getText("UTF-8");
assert buildLog.contains('[WARNING] Could not read content');

return true;
