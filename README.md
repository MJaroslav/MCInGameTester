# MCInGameTester

Engine for tests, that requires minecraft to be loaded.

[![GitHub issues](https://img.shields.io/github/issues/mjaroslav/mcingametester)](https://github.com/mjaroslav/mcingametester/issues "GitHub issues")
[![GitHub forks](https://img.shields.io/github/forks/mjaroslav/mcingametester)](https://github.com/mjaroslav/mcingametester/network "GitHub forks")
[![GitHub stars](https://img.shields.io/github/stars/mjaroslav/mcingametester)](https://github.com/mjaroslav/mcingametester/stargazers "GitHub stars")
[![GitHub license](https://img.shields.io/github/license/mjaroslav/mcingametester)](https://github.com/MJaroslav/mcingametester/blob/master/LICENSE "Open license")
[![JitPack](https://jitpack.io/v/MJaroslav/MCInGameTester.svg)](https://jitpack.io/#MJaroslav/MCInGameTester "JitPack")
[![JitCI status](https://jitci.com/gh/MJaroslav/MCInGameTester/svg)](https://jitci.com/gh/MJaroslav/MCInGameTester "JitCI")
![GitHub CI test status](https://github.com/MJaroslav/MCInGameTester/actions/workflows/ci-test.yml/badge.svg)

## Usage

### Adding to gradle

Just add next line after `apply plugin: 'forge'`:

```groovy
apply from: 'https://raw.githubusercontent.com/MJaroslav/MCInGameTester/master/gradle/configurations/v1.gradle'
```

### Tasks

- `testClient` - runs client with test engine and test source set added in classpath.
- `testServer` - runs server with test engine and test source set added in classpath.
- `testJar` - builds temp jar from test source set. You no needs in this, its utility task.

**Note**: You can configure `testClient`/`testServer` like as `runClient`/`runServer` tasks (it's JavaExec).

**Note**: Any game test task will stop game after all test execution if configuration don't say else.

### Declaring tests

All that you need for making tests, contains in `com.github.mjaroslav.mcingametester.api` (API) package.  
If you need to log something, use `com.github.mjaroslav.mcingametester.lib.ModInfo#LOG` logger.

**Test-like method** - it's a non-static void method without parameters.  
**Test container** - it's a simple class with test-like methods.

**Note:** All annotated things by annotations from API in test containers can be with any access modifier.

For begin, just create test containers in your test source set and then annotate them with `@Client` (for only client
side), `@Server` (for only server side) or `@Common` (for both side).

Now, you can write tests in these classes. Just write test-like methods and annotate them with `@Test`.

If you needs in executing something before or after each test, you can make test-like method but annotate it with `@BeforeEach` or `@AfterEach` instead of `@Test`.

If you needs in executing something before or after all tests in class, you can make **static** test-like method but annotate it with `@BeforeClass` or `@AfterClass` instead of `@Test`.

In addition, for Server sided tests you can make non-static World-typed field with `@WorldShadow` annotation. It's sets Overworld World object to this field.

**Note:** Only logger from `ModInfo` showed in game test tasks by default.

#### Examples

```java
package ...;

import com.github.mjaroslav.mcingametester.api.*;
import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

@Common // Both sides tests.
public class TestCommomSide {
    @BeforeClass
    static void beforeClass() {
        LOG.info("Will executed on both sides before all tests from this class");
    }
    
    @Test
    void test$common() {
        LOG.info("Will executed on both sides");
    }
}
```

```java
package ...;

import com.github.mjaroslav.mcingametester.api.*;
import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

@Server // Server side tests.
public class TestServerSide {
    @AfterEach
    void afterEach() {
        LOG.info("Will executed after each test of this class on server side");
    }
    
    @Test
    void test$server() {
        LOG.info("Will executed on server side");
    }
}
```

### Test execution

Every test can return one of three results:

- `SUCCESS` - expected throwable was thrown (if present in `@Test`) and anything else wasn't thrown.
- `FAILED` - expected throwable wasn't thrown (if present in `@Test`) or `AssertionError` was thrown and anything else
  wasn't thrown.
- `ERROR` - just error in test, for example not expected exception thrown. It will crash the game.

**Note:** For not throwing AssertionErrors in tests manually, you can use `Assert` class from API.

#### Examples

```java
package ...;

import com.github.mjaroslav.mcingametester.api.*;
import cpw.mods.fml.common.FMLCommonHandler;

@Server // Server side tests.
public class TestServerSide {
    @Test(expected = ClassNotFoundException.class)
    void test$clientClass() throws ClassNotFoundException {
        // Will throw expected ClassNotFoundException
        Class.forName("net.minecraft.client.Minecraft");
    }

    @Test
    void test$serverClass() throws ClassNotFoundException {
        // Will crash only on client side with unexpected ClassNotFoundException.
        // Hm... may be @Test needed in 'unexpected' parameter.
        Class.forName("net.minecraft.server.dedicated.DedicatedServer");
    }

    @Test
    void test$fmlSide() {
        // Fails if isServer() return false. It's possible only in client.
        Assert.isTrue(FMLCommonHandler.instance().getSide().isServer(), "Non server side");
    }
}
```

TODO

## Configuration

### Environment variables

TODO

### System properties

TODO