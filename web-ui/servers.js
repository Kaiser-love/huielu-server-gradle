var http = require('http');
var fs = require('fs');
var url = require('url');
var path = require('path');
var server = http.createServer(function (request, response) {
    //获取输入的url解析后的对象
    var pathObj = url.parse(request.url, true);
    //static文件夹的绝对路径
    var staticPath = path.resolve(__dirname, '');
    //获取资源文件绝对路径
    var filePath = path.join(staticPath, pathObj.pathname);
    //异步读取file
    fs.readFile(filePath, 'binary', function (err, fileContent) {
        if (err) {
            console.log('404');
            response.writeHead(404, 'not found');
            response.end('<h1>404 Not Found</h1>')
        } else {
            console.log(request.url);
            response.write(fileContent, 'binary');
            response.end()
        }
    })
});
server.listen(8081);
console.log('visit http://localhost:8081/ui/login.html');
