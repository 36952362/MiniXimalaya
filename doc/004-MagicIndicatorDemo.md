# MagicIndicator项目下载并演示
由于在项目主界面呀使用到指示器的功能，由于有可用的开源项目，直接下载使用即可。
### 1. 在GitHub上下载MagicIndicator
	https://github.com/hackware1993/MagicIndicator
### 2. 导入MagicIndicatro工程并编译使用
**Tips:**

由于下载的开源项目所使用的NDK版本和本地使用的版本不一致，这样在编译开源项目时会导致Android Studio编辑器下载开源项目指定的版本，导致要下载很多的库，并且耗时较常。一个小技巧就是使开源项目中编译和依赖库的版本本地已经下载过的版本保持一致，这样就避免了再次的下载。但是这样并不能保证开源项目能够编译成功，如果有错误就只能回溯修改。

修改的主要文件有:
build.gradle:
gradle-wrapper.properties: