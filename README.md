#开发环境运行
java  -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=dev
java -Xms1024m -Xmx1024m -Dspring.profiles.active=dev -jar rap-data-starter.jar 
#测试环境运行
java  -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=test
java -Xms1024m -Xmx1024m -Dspring.profiles.active=test -jar rap-data-starter.jar 
#生产环境运行
java -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=prod
java -Xms1024m -Xmx1024m -Dspring.profiles.active=prod -jar rap-data-starter.jar 
#上海模拟环境运行
java -Xms1024m -Xmx1024m  -jar rap-data-starter.jar --spring.profiles.active=prod
java -Xms1024m -Xmx1024m -Dspring.profiles.active=prod -jar rap-data-starter.jar 
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


全部

java -Xms1024m -Xmx1024m -Dspring.profiles.active=prod -Dspring.datasource.url=XXX -Dspring.datasource.username=XXX -Dspring.datasource.password=XXX -Des.url=XXX -Des.username=XXX -Des.password=XXX -jar rap-data-starter.jar 