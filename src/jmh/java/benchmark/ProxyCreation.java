package benchmark;

import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Threads(1)
public class ProxyCreation {

  private static final @NotNull Class<?>[] INTERFACES = {MyInterface.class};

  @Benchmark
  public Object newProxyInstance() {
    return Proxy.newProxyInstance(ProxyCreation.class.getClassLoader(), INTERFACES, new Handler());
  }

  @Benchmark
  public Object constructor(ConstructorState state) {
    try {
      return state.constructor.newInstance(new Handler());
    }
    catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  @Benchmark
  public Object manualProxy() {
    return new ManualProxy(new Handler());
  }

  @State(Scope.Benchmark)
  public static class ConstructorState {
    private Constructor<?> constructor;

    @Setup
    public void setUp() {
      try {
        Class<?> proxyClass = java.lang.reflect.Proxy.getProxyClass(ProxyCreation.class.getClassLoader(), INTERFACES);
        constructor = proxyClass.getConstructor(InvocationHandler.class);
      }
      catch (NoSuchMethodException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private interface MyInterface {
    @SuppressWarnings("unused")
    void someMethod() throws Throwable;
  }

  private static final class ManualProxy implements MyInterface {
    private final InvocationHandler handler;

    private ManualProxy(InvocationHandler handler) {
      this.handler = handler;
    }

    @Override
    public void someMethod() throws Throwable {
      handler.invoke(null, null, null);
    }
  }

  private static final class Handler implements InvocationHandler {
    @Override
    @SuppressWarnings("SuspiciousInvocationHandlerImplementation")
    public Object invoke(Object proxy, Method method, Object[] args) {
      return null;
    }
  }
}
