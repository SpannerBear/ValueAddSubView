#ValueAddSubView

一个使用方便,具有一定的自定义能力,过滤掉尽量多的无关数量监听的数值加减控件.

除了通常用法之外,也适合在列表中使用,提供多个setValue重载,减少不必要的数值响应,提升列表性能
并有数值重置功能,使用者只需要专注于数值的监听.

提供以下12个自定义属性
```
        <!--文字颜色-->
        <attr name="vTextColor" format="reference|color"/>
        <!--编辑区背景-->
        <attr name="vTextBg" format="reference|color"/>
        <!--加图标-->
        <attr name="vAddIcon" format="reference"/>
        <!--减图标-->
        <attr name="vSubIcon" format="reference"/>
        <!--默认值-->
        <attr name="vDefaultValue" format="integer"/>
        <!--最大值-->
        <attr name="vMaxValue" format="integer"/>
        <!--最小值-->
        <attr name="vMinValue" format="integer"/>
        <!--编辑区域最大宽度-->
        <attr name="vTextMaxWidth" format="dimension"/>
        <!--编辑区域最小宽度-->
        <attr name="vTextMinWidth" format="dimension"/>
        <!--左右分割线颜色-->
        <attr name="vDividerColor" format="color"/>
        <!--分割线宽度-->
        <attr name="vDividerWidth" format="dimension"/>
        <!--高度,会同时影响左右按键宽高-->
        <attr name="vHeight" format="dimension"/>
```

####依赖到你的项目:
在项目根build.gradle添加以下代码
```
allprojects {
		     repositories {
		    	    ...
		        maven { url 'https://jitpack.io' }
			     	}
	}
```

在module的build.gradle添加依赖
```
	dependencies {
	    	compile 'com.github.User:Repo:Tag'
	}
```