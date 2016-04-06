[![Build Status](https://travis-ci.org/zalando/zester.svg?branch=master)](https://travis-ci.org/zalando/zester)

# Zester
Zester is an IntelliJ IDEA plugin for running mutation tests with a click of a button.
It uses [PIT](http://pitest.org/) under the hood. This plugin exists, because there is currently no simple way in
IntelliJ to run mutation tests.

Zester currently supports Maven and Gradle projects.

## Installation

You can find Zester at [IntelliJ IDEA Plugin Repository](https://plugins.jetbrains.com/plugin/8281).

## Usage
Once the plugin is installed, right click a test file in Project tool window and select Zester configuration.

![alt text](https://github.com/zalando/zester/blob/master/docs/run_zester.png?raw=true "Zester Run")

If the test class is already associated with a run configuration, you need to add Zester from "Edit configurations...".

### Convention over configuration
Zester expects that each unit test is named with a "Test" suffix, for example, _com.company.app.CalculatorTest_.
The source file under test is expected to be in the same package structure with the same name (excluding "Test"), for example,
_com.company.app.Calculator_.

If your file structure is different you can provide a more detailed configuration in "Run Configurations".

## Development

Developing the plugin is easy, just run

```./gradlew runIdea```

This will start IntelliJ IDEA with the plugin installed.

## How to Contribute
If you want to contribute, please fork and create a pull request.

### Bug Fixes
If you find a bug, it would be awesome if you created an issue about it. Please include a clear description of the problem so that we can fix it!

## Contact:
sebastian.monte@zalando.de

## License
The MIT License (MIT) Copyright © 2016 Zalando SE, https://tech.zalando.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.