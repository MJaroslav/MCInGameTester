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

**Note:** This build script extension just adds some tasks and last project version to dependencies. If you want to use
specific version of engine, add next to your build script:

```groovy
configurations.all {
    resolutionStrategy {
        force 'com.github.MJaroslav:MCInGameTester:VERSION:dev'
    }
}
```

Where `VERSION` is a selected engine version.

### Tasks

- `testClient` - runs client with test engine and test source set added in classpath.
- `testServer` - runs server with test engine and test source set added in classpath.
- `testJar` - builds temp jar from test source set. You no needs in this, its utility task.

**Note**: Any game test task will stop game after all test execution if configuration don't say else.

### Declaring tests

All that you need for making tests, contains in `com.github.mjaroslav.mcingametester.api` (API) package.  
If you need to log something, use `com.github.mjaroslav.mcingametester.lib.ModInfo#LOG` logger.

**Test-like method** - it's a non-static void method without parameters.  
**Test container** - it's a simple class with test-like methods.

**Note:** All annotated things by annotations from API in test containers can be with any access modifier.

For begin, just create test containers in your test source set and then annotate them with `@Client` (for only client
side), `@Server` (for only server side) or `@Common` (for both side).

**Note:** For these three annotations, you can use  `when` parameter with `LoaderState` type for setting loading state
when tests should be run.

Now, you can write tests in these classes. Just write test-like methods and annotate them with `@Test`.

If you needs in executing something before or after each test, you can make test-like method but annotate it
with `@BeforeEach` or `@AfterEach` instead of `@Test`.

If you needs in executing something before or after all tests in class, you can make **static** test-like method but
annotate it with `@BeforeClass` or `@AfterClass` instead of `@Test`.

In addition, for Server sided tests you can make non-static World-typed field with `@WorldShadow` annotation. It's sets
Overworld World object to this field.

**Note:** Only logger from `ModInfo` showed in game test tasks by default.

#### Examples

```java
// Tests from this test container will executed on both sides.
@Common
public class TestCommomSide {
    // Be careful, all before/after methods should not throwing any exceptions.
    @BeforeClass
    static void beforeClass() {
        // Static imported LOG from ModInfo.
        LOG.info("Will executed on both sides before all tests from this class");
    }
    
    @Test
    void test$common() {
        LOG.info("Will executed on both sides");
    }
}

// Tests from this test container will executed only on server side.
@Server(when = LoaderState.INITIALIZATION) // Tests will executed in end (after other mods) of initialization state.
public class TestServerSide {
    @WorldShadow
    World overworld; // Getter for server Overworld (dim 0). 

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

### Test writing

Every test can return one of three results:

- `SUCCESS` - expected throwable was thrown (if present by `expected` parameter in `@Test`) and anything else wasn't
  thrown.
- `FAILED` - expected throwable wasn't thrown (if present by `expected` parameter in `@Test`) or `AssertionError` was
  thrown and anything else
  wasn't thrown.
- `ERROR` - just error in test engine, for example bad test syntax. It will crash the game.

**Note:** For not throwing AssertionErrors in tests manually, you can use `Assert` class from API. You can also use same
name class from JUnit or any helper for throwing `AssertionError` from another libs.

#### Examples

```java
@Test(expected = ClassNotFoundException.class)
void test$clientClassOnServer() throws ClassNotFoundException {
    // Test will cause ClassNotFoundException on server and fail test.
    // All unexpected exceptions will wrapped in AssertionError and cause test fail.
    Class.forName("net.minecraft.client.Minecraft"); 
   // Use deobf names without any problems.
   // I hope you don't run this in obfuscated environment, do you?
}

@Test
void test$isServerSide() {
    // Fails test if condition return false.
    Assert.isTrue(FMLCommonHandler.instance().getSide().isServer(), "Non server side");
}

@WorldShadow // Only for server side test containers.
World overworld;

@Test
void test$shadowWorld() {
    // You can combine several assertions.
    Assert.isTrue(overworld != null, "Null world");
    Assert.isEquals(overworld.provider.dimensionId, 0, "Not Overworld");
}
```

### Test execution

For run tests manually you should execute `testClient` and/or `testServer` task.

## Configuration

### Engine options

You can change next options of engine by system environments or JVM system properties (`-Dkey=value`):

|                                 Parameter                                  |                                                                        Description                                                                        |                   Default value                   |
|:--------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------:|
|    MCIGT_STOP_AFTER_SUCCESS<br/>or<br/>MCInGameTester.stopAfterSuccess     |                                                         Stop game after success tests execution.                                                          |                       true                        |
| MCIGT_FORCED_GAME_STOP_STATE<br/>or<br/>MCInGameTester.forcedGameStopState | Forces LoaderState for game stopping. All tests in test containers where `when` parameter set with LoaderState that should be after forced, will ignored. | Used max value from annotations `when` parameter. |
|         MCIGT_STOP_NO_TESTS<br/>or<br/>MCInGameTester.stopNoTests          |                                                               Stop game if no tests loaded.                                                               |                       true                        |
|       MCIGT_STOP_FIRST_FAIL<br/>or<br/>MCInGameTester.stopFirstFail        |                                                              Stop game on first failed test.                                                              |                       false                       |
|             MCIGT_HALT_EXIT<br/>or<br/>MCInGameTester.haltExit             |                                                 Use System.halt instead of System.exit for game stopping.                                                 |                       false                       |

**Note:** Value from environment variable will force JVM system property value.

### Build script

You can configure `testClient`/`testServer` like as `runClient`/`runServer` tasks (it's JavaExec-typed tasks), but these
tasks contains some additional parameters:

```groovy
testServer { // or testClient
    // Recreate working dir before game launch.
    clearWorkingDirBeforeLaunch = true
    // Auto accepting eula for server.
    eula = true // (false for testClient)
    // Use custom Log4J configuration (mcingametester.xml) for showning only ModInfo#LOG logger.
    logOnlyTests = true
    // Copies mapping to `../conf` of working directory. It's can be useful
    // if you use CodeChickenLib in project: version from GregTech repository 
    // can find mappings automatically, witout showing Java Swing File Chooser.
    copyMappingsLocally = true
    
    // In additional, testServer have default "nogui" program argument.
}
```

Script extension also can set  `testClient` and `testServer` as `test` task dependencies if `CI` environment variable
is `true`, but for client also required environment variable `MCIGT_HAS_DISPLAY` with same value. It's required because
OS can be headless and client game will crash. In this case use any display faker for it, for example XVFB.

## CI environments

### GitHub actions

This action will run all tests (`test` with `testClient` and `testServer` as dependencies) on any push or pull request.

```yaml
name: Run gradle tests

on: [push, pull_request]

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 8
      uses: actions/setup-java@v3
      with:
        java-version: '8'
        distribution: 'temurin'
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    - name: Run all tests headless
      uses: GabrielBB/xvfb-action@v1
      with:
        run: ./gradlew test
      env:
        MCIGT_HAS_DISPLAY: true
```

### JitCI

Thanks jitpack.io command for adding XVFB to their docker containers by my request.

I use next non-standard settings for this project:

- Environment variable `MCIGT_HAS_DISPLAY` that equals `true`
- Replace test command
  by `xvfb-run -e /dev/stdout -s "-screen 0 1280x1024x24 -ac -nolisten tcp -nolisten unix" -a ./gradlew test`

**Note:** Disable dependency cache in init command if you have `cache('http')` or something problem.

**Note:** In `jitpack.yml` I use next configuration for building without CI (but you can try use XVFB too):

```yaml
jdk:
  - openjdk8
install:
  - ./gradlew build publishToMavenLocal
env:
  CI: false
```

## Released and planned features

- [X] "Engine".
- [X] Run tasks.
- [X] CI integration.
- [ ] Real Gradle Plugin.
- [ ] JUtil implementation.
- [ ] JUnit report implementation.
- [ ] Port to next game versions without alternative frameworks.
- [ ] Implement fake display or make mocked client graphics.

## Post Scriptum

Feel free to correct typos and errors in the text or code :)
