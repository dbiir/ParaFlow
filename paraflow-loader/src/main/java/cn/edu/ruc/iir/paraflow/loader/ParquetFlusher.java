//package cn.edu.ruc.iir.paraflow.loader;
//
//import cn.edu.ruc.iir.paraflow.metaserver.proto.MetaProto;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.parquet.column.ParquetProperties;
//import org.apache.parquet.example.data.Group;
//import org.apache.parquet.example.data.GroupFactory;
//import org.apache.parquet.example.data.simple.SimpleGroupFactory;
//import org.apache.parquet.hadoop.ParquetSegmentWriter;
//import org.apache.parquet.hadoop.example.GroupWriteSupport;
//import org.apache.parquet.schema.MessageType;
//import org.apache.parquet.schema.MessageTypeParser;
//
//import java.io.IOException;
//
///**
// * paraflow
// *
// * @author guodong
// */
//public class ParquetFlusher extends DataFlusher
//{
//    private Configuration conf = new Configuration();
//
//    public ParquetFlusher(String threadName)
//    {
//        super(threadName);
//        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
//        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
//    }
//
//    @Override
//    boolean flushData(BufferSegment segment)
//    {
//        System.out.println("Parquet file flush out!!!");
//        String topic = segment.getFiberPartitions().get(0).getTopic();
//        int indexOfDot = topic.indexOf(".");
//        String dbName = topic.substring(0, indexOfDot);
//        int length = topic.length();
//        String tblName = topic.substring(indexOfDot + 1, length);
//        long beginTime = segment.getTimestamps()[0];
//        long endTime = segment.getTimestamps()[0];
//        for (long timeStamp : segment.getTimestamps()) {
//            if (timeStamp < beginTime) {
//                beginTime = timeStamp;
//            }
//            if (timeStamp > endTime) {
//                endTime = timeStamp;
//            }
//        }
//        String path = FileNameGenerator.generator(dbName, tblName, beginTime, endTime);
//        MetaProto.StringListType columnsNameList = metaClient.listColumns(dbName, tblName);
//        MetaProto.StringListType columnDataTypeList = metaClient.listColumnsDataType(dbName, tblName);
//        int columnNameCount = columnsNameList.getStrCount();
//        int columnDataTypeCount = columnDataTypeList.getStrCount();
//        System.out.println("Path: " + path);
//        if (columnNameCount == columnDataTypeCount) {
//            Path file = new Path(path);
//            try {
//                StringBuilder schemaString = new StringBuilder("message " + tblName + " {");
//                for (int i = 0; i < columnDataTypeCount; i++) {
//                    switch (columnDataTypeList.getStr(i)) {
//                        case "bigint":
//                            schemaString.append("required INT64 ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        case "int":
//                            schemaString.append("required INT32 ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        case "boolean":
//                            schemaString.append("required BOOLEAN ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        case "float4":
//                            schemaString.append("required FLOAT ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        case "float8":
//                            schemaString.append("required DOUBLE ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        case "timestamptz":
//                            schemaString.append("required INT64 ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        case "real" :
//                            schemaString.append("required INT64 ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                        default:
//                            schemaString.append("required BINARY ").append(columnsNameList.getStr(i)).append("; ");
//                            break;
//                    }
//                }
//                schemaString.append("}");
//                System.out.println("Parquet file schema: " + schemaString);
//                MessageType schema = MessageTypeParser.parseMessageType(schemaString.toString());
//                GroupFactory factory = new SimpleGroupFactory(schema);
//                GroupWriteSupport writeSupport = new GroupWriteSupport();
//                GroupWriteSupport.setSchema(schema, conf);
//                ParquetSegmentWriter<Group> writer = new ParquetSegmentWriter<Group>(file, writeSupport,
//                        ParquetSegmentWriter.DEFAULT_COMPRESSION_CODEC_NAME,
//                        ParquetSegmentWriter.DEFAULT_BLOCK_SIZE,
//                        ParquetSegmentWriter.DEFAULT_PAGE_SIZE,
//                        ParquetSegmentWriter.DEFAULT_PAGE_SIZE,
//                        false,
//                        ParquetSegmentWriter.DEFAULT_IS_VALIDATING_ENABLED,
//                        ParquetProperties.WriterVersion.PARQUET_2_0,
//                        conf
//                );
//                while (segment.hasNext()) {
//                    String[] contents = segment.getNext();
//                    Group group = factory.newGroup();
//                    for (int i = 0; i < contents.length; i++) {
//                        switch (columnDataTypeList.getStr(i)) {
//                            case "bigint":
//                                group.append(columnsNameList.getStr(i), Long.parseLong(contents[i]));
//                                break;
//                            case "int":
//                                group.append(columnsNameList.getStr(i), Integer.parseInt(contents[i]));
//                                break;
//                            case "boolean":
//                                group.append(columnsNameList.getStr(i), Boolean.parseBoolean(contents[i]));
//                                break;
//                            case "float4":
//                                group.append(columnsNameList.getStr(i), Float.parseFloat(contents[i]));
//                                break;
//                            case "float8":
//                                group.append(columnsNameList.getStr(i), Double.parseDouble(contents[i]));
//                                break;
//                            case "timestamptz":
//                                group.append(columnsNameList.getStr(i), Long.parseLong(contents[i]));
//                                break;
//                            case "real" :
//                                group.append(columnsNameList.getStr(i), Float.parseFloat(contents[i]));
//                                break;
//                            default:
//                                group.append(columnsNameList.getStr(i), contents[i]);
//                                break;
//                        }
//                    }
//                    writer.write(group);
//                }
//                writer.close();
//                segment.setFilePath(path);
//                return true;
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        else {
//            return false;
//        }
//    }
//}
