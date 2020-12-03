# libreoffice
## 一键转换office等文档为pdf，支持本地与远程转化，需要结合SpringBoot-JodConverter，以及LibreOffice，本地下载LibreOffice在ymal文件中配置，远程访问需要结合collabora，在docker中进行运行
```
docker run -t -d -p 127.0.0.1:9980:9980 -e "domain=" -e "username=admin" -e "password=S3cRet" --restart always collabora/code
```
详细看官方文档：https://www.collaboraoffice.com/code/docker/
