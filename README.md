[![Build Status](https://travis-ci.org/zalando/zester.svg?branch=master)](https://travis-ci.org/zalando/zester)

# Zester
Zester is an IntelliJ IDEA plugin for running mutation tests with a click of a button. It uses [PIT](http://pitest.org/) under the hood. This plugin exists to provide a simple way to run mutation tests in IntelliJ; we don't know of another way that's as simple or simpler.

Zester currently supports Maven and Gradle projects.

### Installation

You can find Zester at [IntelliJ's IDEA Plugin Repository](https://plugins.jetbrains.com/plugin/8281).

### Usage
After installing Zester, right click a test file in the Project tool window and select the Zester configuration.

**Note:** If the test class is already associated with a run configuration (for example, JUnit runner), you need to add Zester from "Edit configurations...".

<a href="https://github.com/zalando/zester/blob/master/docs/run_zester.png?raw=true" target="_blank"><img src="https://github.com/zalando/zester/raw/master/docs/run_zester.png?raw=true" alt="alt text" title="Zester Run" style="width: 350px;"></a>

### Convention Over Configuration
Zester expects that each unit test is named with a "Test" suffix — for example, _com.company.app.CalculatorTest_.
The source file under test should follow the same package structure and include the same name, but without "Test" at the end. For example: _com.company.app.Calculator_.

If your file structure is different, you can provide a more detailed configuration in "Run Configurations".

### Development

Developing Zester is easy. Just run:

```
./gradlew runIdea
```

This will start IntelliJ IDEA with the plugin installed.

### How to Contribute
If you want to contribute, please fork and create a pull request.

### Bug Fixes
If you find a bug, please create an issue with a clear description of the problem so that we can fix it!

### Contact:
sebastian.monte@zalando.de

### FAQ

> I am not able to exclude multiple classes/packages.

Make sure you don't have any spaces between the excluded classes or packages. The following would only exlude `package1`.

```
--excludedClasses package1., package2.
```

Correct format:
```
--excludedClasses package1.,package2.
```

### License
The MIT License (MIT) Copyright © 2016 Zalando SE, https://tech.zalando.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
