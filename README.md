# white demo android

## TOC

- [快速调试](#快速调试)
- [回放带音视频的白板](#回放带音视频的白板)
- [HttpDNS使用](#HttpDNS)
- [动态资源包预加载](#动态资源包预加载)

## 快速调试

>快速调试需要开发者已经阅读过 https://developer.netless.link 快速集成部分，获取 AppIdentifier 等部分内容。

修改 `DemoAPI` 类中，填入需要调试的 room uuid 与 roomToken， App Identifier。
```Java
    private String AppIdentifier = "";
    private String demoUUID = "";
    private String demoRoomToken = "";
```

启用应用，点击【加入白板】，会自动进入该房间；点击【加入回放】，会自动查看该房间回放。

## 回放带音视频的白板

demo 中，已添加 ExoPlayer 与 IjkPlayer 支持，开发者可以参考 `5cd82cc` commit 中的改动。

主要内容内容：
1. 集成 ijk 与 exo
1. 添加对应架构的 so 文件
1. 修改 PlayerActivity，将使用原生组件实现的 NativePlayer 接口的 NativeMediaPlayer 类替换为 WhiteExoPlayer 或者 WhiteIjkPlayer 进行播放

## HttpDNS

1. 根据阿里云 httpdns 文档，进行 [Maven集成](https://help.aliyun.com/document_detail/30140.html)
1. 根据[Android Webview + HttpDns最佳实践](https://help.aliyun.com/document_detail/60181.html?spm=5176.doc30144.6.565.1qauiM)对 WhiteboardView 进行拦截。

具体操作，请查看 git commit 中 `add ali httpdns class`, `httpdns` 记录。

>2.8.0开始，已经有`sdk`内部已经有更多线路选择，httpdns 实现，只有一小部分线路能够被代理，不再被推荐。

## 动态资源包预加载

从 Android API 21 开始，Android 官方提供拦截 WebView 网络请求，替换响应的功能。基于该API，我们可以将动态 ppt 所需要的网络资源（服务器端已提供zip下载）先下载到应用本地。然后再将所有 WebView 的请求，与本地应用内文件做匹配，如果存在，则使用本地请求。

实现逻辑：

1. 提前下载特定动态 ppt 资源包
    * 建议提前下载，在课程中下载，由于当时已经使用音视频，可能会有较大的带宽负担，造成用户体验大幅度下降
    * 具体下载方式，请看 https://developer.netless.link/ 中查看【server】端文档中的【动态转换资源包】
    * 最佳方式：老师提前备课，后端记录白板房间中动态 ppt 的 taskUUID
2. 解压动态资源包至应用中的某个路径
3. 拦截白板 WebView 中，对 动态 ppt 资源的请求，进行拦截替换（特定域名）
4. 在拦截 API 中，查找对应文件，构造正确的 response
5. 在合适的时机，清理下载的资源包

demo 中已经有基本实现，具体请看`dynamic PPT zip` commit 中代码。

> 1. 请注意，该 API 的支持版本为 Android API 21
> 2. 由于国内 ROM 的 WebView 情况比较复杂，可能会有兼容性问题，请在应用内设置成开关，以避免用户系统不兼容，造成无法观看动态 ppt 中的音视频。
> 3. 动态 ppt 资源大小与原始 ppt 文件有关。建议在使用后，进行删除，以免占用过多的用户空间。