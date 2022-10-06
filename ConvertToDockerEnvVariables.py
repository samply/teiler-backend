#python
#exec(open("ConvertToDockerEnvVariables.py").read())
envFile = open(".env","r")
result = ""
for line in envFile:
     if len(line) > 1:
         elements = line.split("=");
         result += elements[0] + ": " + '\"' + elements[1][0:(len(elements[1])-1)]+'\"\r\n';
print(result)
