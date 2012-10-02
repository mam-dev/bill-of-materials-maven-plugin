File buildLogFile = new File( basedir, "build.log" );
assert buildLogFile.exists();
String buildLog = buildLogFile.getText("UTF-8");
assert buildLog.contains(":read-bill-of-materials");

File bomFile = new File( basedir, "target/tickets/bill-of-materials.txt" );
assert bomFile.exists();

String bomFileContent = bomFile.getText("UTF-8");
assert bomFileContent.readLines().size() == 10;

return true;
