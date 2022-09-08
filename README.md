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

Just create classes in your test source set, then annotate them with `@Client` (for only client side),
`@Server` (for only server side) or `@Common` (for both side).

**Note**: All methods and fields in next instructions can be with any access modifier.

Now, you can write test in these classes. Test is annotated with `@Test` void method without parameters.

If you needs in executing something before or after each test, you can make test-like method but annotate it with \
`@BeforeEach` or `@AfterEach` instead of `@Test`.

If you needs in executing something before or after all tests in class, you can make static test-like method but
annotate it with `@BeforeClass` or `@AfterClass` instead of `@Test`.

In addition, for Server sided tests you can make non-static World-typed field with `@WorldShadow` annotation. \
It's sets Overworld World object to this field.

**Note:** Only logger from `ModInfo.LOG` showed in game test tasks by default.

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
public class TestClientSide {
    @AfterEach
    void afterEach() {
        LOG.info("Will executed after each test of this class on client side");
    }
    
    @Test
    void test$common() {
        LOG.info("Will executed on client side");
    }
}
```

### Test execution

TODO

## Configuration

### Environment variables

TODO

### System properties

TODO
