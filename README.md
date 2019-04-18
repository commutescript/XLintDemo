## 自定义lint
### 背景
在进行基础架构和组件迁移重构的过程中，需要对工程中的资源命名格式、组件及资源大小进行一定的限制。经过调研，可以使用自定义lint + 自定义plugin的形式，快速无缝接入各业务项目以及CI。如果需要通过plugin无缝接入，可以将xlint推送到远程maven仓库，再在plugin中接入jar包。

### 已有Detector

* ImageDetector：检测图片资源的大小
* ThreadDetector：检测线程的使用
* ToastDetector：检测toast的使用
* XLogDetector：检测log的使用
* XRouterDetector：检测路由的使用
* LayoutNameDetector：检测layout命名
* ViewIdDetector：检测view id命名

### 基本API
lint相关的api在版本迭代过程中除了扫描Java的类扫描比较大，其他都变化不大，这里以lint-api:26.2.1为例，查看Detector抽象类，包括如下几种接口：

* UastScanner（实际调用SourceCodeScanner接口）：扫描Java源文件
* ClassScanner：扫描class文件
* BinaryResourceScanner：扫描二进制资源
* ResourceFolderScanner：扫描资源文件
* XmlScanner：扫描xml
* GradleScanner：扫描gradle
* OtherFileScanner：扫描其他文件


扫描Java文件的类变化比较大，主要分为3个阶段。第一阶段，Android Studio 2.2 和lint-api 25.2.0以下，你需要实现JavaScanner接口；第二阶段，在Android Studio 3.0 和lint-api 25.4.0以下，你需要实现JavaPsiScanner接口；第三阶段，在Android Studio 3.0 和lint-api 25.4.0以上，实现UastScanner接口。其用法都大同小异，相互之间也很容易迁移。

```
    // 遍历的元素
    open fun getApplicableElements(): Collection<String>? = null
    
    // 遍历的属性
    open fun getApplicableAttributes(): Collection<String>? = null

    // 遍历的名字
    open fun getApplicableCallNames(): List<String>? = null

    // 遍历的调用者
    open fun getApplicableCallOwners(): List<String>? = null

    // 遍历的ASM节点
    open fun getApplicableAsmNodeTypes(): IntArray? = null

    // 遍历的文件
    open fun getApplicableFiles(): EnumSet<Scope> = Scope.OTHER_SCOPE

    // 遍历的方法
    open fun getApplicableMethodNames(): List<String>? = null

    // 遍历的构造器
    open fun getApplicableConstructorTypes(): List<String>? = null

    // 遍历的Psi类型
    open fun getApplicablePsiTypes(): List<Class<out PsiElement>>? = null

    // 遍历的对象名字
    open fun getApplicableReferenceNames(): List<String>? = null

    // 遍历的Uast类型
    open fun getApplicableUastTypes(): List<Class<out UElement>>? = null
    
    // 遍历的构造器
    open fun visitConstructor(
        context: JavaContext,
        node: UCallExpression,
        constructor: PsiMethod
    ) {
    }

   // 遍历方法
    open fun visitMethod(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
    }

```

当然主要的API并不限于这些，可以通过源码自行查阅。


### 其他
自定义lint的扫描范围比较广，因此应用范围也比较广，比如检查命名、检查方法的使用、检查资源文件的大小等。Gradle、Plugin相关知识及具体的技术细节可参考公众号“小工说”的《Gradle实战》及《自定义lint实战》。



