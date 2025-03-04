#python
#exec(open("ConcatEnvVariables.py").read())
envFile = open(".env","r")
result = ""
for line in envFile:
     if len(line) > 1:
         line = line.replace("\r", "")
         line = line.replace("\n", "")
         result += line + ";"
print(result[0:(len(result)-1)])
