#开发环境运行
java  -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=dev

#测试环境运行
java  -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=test

#生产环境运行
java -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=prod

#上海模拟环境运行
java -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=prod

#开发es配置
-Drap.es.url=30.40.36.183:3006
-Drap.es.username=elastic
-Drap.es.password=8RWTLqfPPrjXnTWoplWylldOS_K9u

#注意事项
若同时开启丁税宝和税小蜜同步application.properties中
is_dsb_open=true
is_bot_open=true

若只开启丁税宝同步
is_dsb_open=true
is_bot_open=false

若只开启税小蜜同步
is_dsb_open=false
is_bot_open=true