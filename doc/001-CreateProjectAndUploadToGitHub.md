# 创建项目并上传到GitHub
### * 在Android Studio中安装向导创建一个空项目
### * 在Github中也创建一个空项目并拿到项目地址的URL
### * 使用一系列Git命令把本地项目上传到GitHub
	cd <localProjectDir>
	
	git init
	
	git add . 
	
	git commit -m "first commit"
	
	git remote add origin https://<repo address>
	
	git push -u origin master