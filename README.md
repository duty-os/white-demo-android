# white demo android

## 快速调试

>快速调试需要开发者已经阅读过 https://developer.netless.link 快速集成部分，获取 AppIdentifier 等部分内容。

修改 DemoAPI 类中，讲已有的 room uuid 与 roomToken 填入在以下字段中
```Java
    private String AppIdentifier = "";
    private String demoUUID = "";
    private String demoRoomToken = "";
```

直接启动demo，点击加入白板，进入该房间；或者点击加入回放，查看房间回放。

## 回放带音视频的白板

demo 中，已添加 ExoPlayer 与 IjkPlayer 支持，开发者可以参考 5cd82cc commit 中的信息。
主要内容内容：
1. 集成 ijk 与 exo
1. 添加对应架构的 so 文件
1. 修改 PlayerActivity，将使用原生组件实现的 NativePlayer 接口的 NativeMediaPlayer 类替换为 WhiteExoPlayer 或者 WhiteIjkPlayer 进行播放

## Httpdns 使用

1. 根据阿里云 httpdns 文档，进行 [Maven集成](https://help.aliyun.com/document_detail/30140.html)
1. 根据[Android Webview + HttpDns最佳实践](https://help.aliyun.com/document_detail/60181.html?spm=5176.doc30144.6.565.1qauiM)对 WhiteboardView 进行拦截。

具体操作，请查看 git commit 中 `add ali httpdns class`, `httpdns` 记录。