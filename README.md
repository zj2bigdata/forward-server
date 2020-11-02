# storage
内外网交换

实现网络隔离

利用rabbitmq实现对接口的请求代理



1. frowrad负责对接口进行监听并将请求转发至rabbitmq,同时监听rabbitmq的返回值

2. forward-server负责处理rabbitmq里的httq请求,并将请求还原再执行,得到请求的真实返回值后再写入rabbitmq

   