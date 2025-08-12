# Storch-Milvus

## Introduction
Storch-Milvus is a project that integrates the power of Storch with Milvus, a high-performance vector database. This integration enables developers to efficiently manage and query vector data, which is crucial for various applications such as machine learning, recommendation systems, and image search.

## Project Structure
The project is structured using Scala and SBT (Scala Build Tool). It includes `.proto` files for defining gRPC services and messages, and Scala source files for implementing the business logic.

## Dependency Installation
To use Storch-Milvus in your project, you need to add the following dependencies to your `build.sbt` file:

```scala 3
libraryDependencies += "io.github.mullerhai" % "storch-milvus_3" % "0.1.1-2.6.1"
```

```scala:D:\data\git\storch-milvus\build.sbt
ThisBuild / scalaVersion := "3.6.4"

libraryDependencies ++= Seq(
  "com.google.api.grpc" % "proto-google-common-protos" % "2.54.1",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "1.0.0-alpha.1",
  "com.thesamet.scalapb" %% "scalapb-runtime" % "1.0.0-alpha.1"
)
```
```
import io.grpc.ManagedChannelBuilder
import milvus.proto.milvus.MilvusServiceGrpc
import milvus.proto.common.MsgBase

object MilvusClientExample extends App {
// Create a channel to the Milvus server
val channel = ManagedChannelBuilder.forAddress("localhost", 19530).usePlaintext().build()
val stub = MilvusServiceGrpc.blockingStub(channel)

// Create a MsgBase instance
val msgBase = MsgBase.newBuilder()
.setMsgType(milvus.proto.common.MsgType.Connect)
.setMsgID("12345")
.setTimestamp(System.currentTimeMillis())
.build()

// Create a ConnectRequest
val connectRequest = milvus.proto.milvus.ConnectRequest.newBuilder()
.setBase(msgBase)
.build()

// Send the request and get the response
val connectResponse = stub.connect(connectRequest)
println(s"Connect response: ${connectResponse.getStatus.getReason}")

// Shutdown the channel
channel.shutdown()
}
```` 

```scala 3

import milvus.proto.milvus.CreateCollectionRequest
import milvus.proto.schema.CollectionSchema

// Create a collection schema
val collectionSchema = CollectionSchema.newBuilder()
  .setName("example_collection")
  .setDescription("An example collection")
  .build()

// Create a CreateCollectionRequest
val createCollectionRequest = CreateCollectionRequest.newBuilder()
  .setBase(msgBase)
  .setDbName("default")
  .setCollectionName("example_collection")
  .setSchema(collectionSchema.toByteArray)
  .build()

// Send the request
val createCollectionResponse = stub.createCollection(createCollectionRequest)
println(s"Create collection response: ${createCollectionResponse.getReason}")
```