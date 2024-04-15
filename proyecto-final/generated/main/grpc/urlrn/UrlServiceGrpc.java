package urlrn;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.39.0)",
    comments = "Source: Url.proto")
public final class UrlServiceGrpc {

  private UrlServiceGrpc() {}

  public static final String SERVICE_NAME = "urlrn.UrlService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<urlrn.Url.urlRequest,
      urlrn.Url.listadoUrls> getObtenerUrlsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "obtenerUrls",
      requestType = urlrn.Url.urlRequest.class,
      responseType = urlrn.Url.listadoUrls.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.Url.urlRequest,
      urlrn.Url.listadoUrls> getObtenerUrlsMethod() {
    io.grpc.MethodDescriptor<urlrn.Url.urlRequest, urlrn.Url.listadoUrls> getObtenerUrlsMethod;
    if ((getObtenerUrlsMethod = UrlServiceGrpc.getObtenerUrlsMethod) == null) {
      synchronized (UrlServiceGrpc.class) {
        if ((getObtenerUrlsMethod = UrlServiceGrpc.getObtenerUrlsMethod) == null) {
          UrlServiceGrpc.getObtenerUrlsMethod = getObtenerUrlsMethod =
              io.grpc.MethodDescriptor.<urlrn.Url.urlRequest, urlrn.Url.listadoUrls>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "obtenerUrls"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.Url.urlRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.Url.listadoUrls.getDefaultInstance()))
              .setSchemaDescriptor(new UrlServiceMethodDescriptorSupplier("obtenerUrls"))
              .build();
        }
      }
    }
    return getObtenerUrlsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<urlrn.Url.urlResponse,
      urlrn.Url.urlResponse> getCrearUrlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "crearUrl",
      requestType = urlrn.Url.urlResponse.class,
      responseType = urlrn.Url.urlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<urlrn.Url.urlResponse,
      urlrn.Url.urlResponse> getCrearUrlMethod() {
    io.grpc.MethodDescriptor<urlrn.Url.urlResponse, urlrn.Url.urlResponse> getCrearUrlMethod;
    if ((getCrearUrlMethod = UrlServiceGrpc.getCrearUrlMethod) == null) {
      synchronized (UrlServiceGrpc.class) {
        if ((getCrearUrlMethod = UrlServiceGrpc.getCrearUrlMethod) == null) {
          UrlServiceGrpc.getCrearUrlMethod = getCrearUrlMethod =
              io.grpc.MethodDescriptor.<urlrn.Url.urlResponse, urlrn.Url.urlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "crearUrl"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.Url.urlResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  urlrn.Url.urlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UrlServiceMethodDescriptorSupplier("crearUrl"))
              .build();
        }
      }
    }
    return getCrearUrlMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UrlServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UrlServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UrlServiceStub>() {
        @java.lang.Override
        public UrlServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UrlServiceStub(channel, callOptions);
        }
      };
    return UrlServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UrlServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UrlServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UrlServiceBlockingStub>() {
        @java.lang.Override
        public UrlServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UrlServiceBlockingStub(channel, callOptions);
        }
      };
    return UrlServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UrlServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UrlServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UrlServiceFutureStub>() {
        @java.lang.Override
        public UrlServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UrlServiceFutureStub(channel, callOptions);
        }
      };
    return UrlServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class UrlServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void obtenerUrls(urlrn.Url.urlRequest request,
        io.grpc.stub.StreamObserver<urlrn.Url.listadoUrls> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getObtenerUrlsMethod(), responseObserver);
    }

    /**
     */
    public void crearUrl(urlrn.Url.urlResponse request,
        io.grpc.stub.StreamObserver<urlrn.Url.urlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCrearUrlMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getObtenerUrlsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                urlrn.Url.urlRequest,
                urlrn.Url.listadoUrls>(
                  this, METHODID_OBTENER_URLS)))
          .addMethod(
            getCrearUrlMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                urlrn.Url.urlResponse,
                urlrn.Url.urlResponse>(
                  this, METHODID_CREAR_URL)))
          .build();
    }
  }

  /**
   */
  public static final class UrlServiceStub extends io.grpc.stub.AbstractAsyncStub<UrlServiceStub> {
    private UrlServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UrlServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UrlServiceStub(channel, callOptions);
    }

    /**
     */
    public void obtenerUrls(urlrn.Url.urlRequest request,
        io.grpc.stub.StreamObserver<urlrn.Url.listadoUrls> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getObtenerUrlsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void crearUrl(urlrn.Url.urlResponse request,
        io.grpc.stub.StreamObserver<urlrn.Url.urlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCrearUrlMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UrlServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<UrlServiceBlockingStub> {
    private UrlServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UrlServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UrlServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public urlrn.Url.listadoUrls obtenerUrls(urlrn.Url.urlRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getObtenerUrlsMethod(), getCallOptions(), request);
    }

    /**
     */
    public urlrn.Url.urlResponse crearUrl(urlrn.Url.urlResponse request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCrearUrlMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UrlServiceFutureStub extends io.grpc.stub.AbstractFutureStub<UrlServiceFutureStub> {
    private UrlServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UrlServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UrlServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.Url.listadoUrls> obtenerUrls(
        urlrn.Url.urlRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getObtenerUrlsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<urlrn.Url.urlResponse> crearUrl(
        urlrn.Url.urlResponse request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCrearUrlMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_OBTENER_URLS = 0;
  private static final int METHODID_CREAR_URL = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UrlServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UrlServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_OBTENER_URLS:
          serviceImpl.obtenerUrls((urlrn.Url.urlRequest) request,
              (io.grpc.stub.StreamObserver<urlrn.Url.listadoUrls>) responseObserver);
          break;
        case METHODID_CREAR_URL:
          serviceImpl.crearUrl((urlrn.Url.urlResponse) request,
              (io.grpc.stub.StreamObserver<urlrn.Url.urlResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class UrlServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UrlServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return urlrn.Url.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UrlService");
    }
  }

  private static final class UrlServiceFileDescriptorSupplier
      extends UrlServiceBaseDescriptorSupplier {
    UrlServiceFileDescriptorSupplier() {}
  }

  private static final class UrlServiceMethodDescriptorSupplier
      extends UrlServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UrlServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UrlServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UrlServiceFileDescriptorSupplier())
              .addMethod(getObtenerUrlsMethod())
              .addMethod(getCrearUrlMethod())
              .build();
        }
      }
    }
    return result;
  }
}
