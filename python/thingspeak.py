import httplib, urllib
# download from <a href="http://code.google.com/p/psutil/" title="http://code.google.com/p/psutil/">http://code.google.com/p/psutil/</a>
import psutil
import time
 
def doit():
    cpu_pc = psutil.cpu_percent()
    mem_avail_mb = psutil.avail_phymem()/1000000
    print cpu_pc
    print mem_avail_mb
    params = urllib.urlencode({'field1': cpu_pc, 'field2': mem_avail_mb,'key':'662EQ8O35OEBQ547'})
    headers = {"Content-type": "application/x-www-form-urlencoded","Accept": "text/plain"}
    conn = httplib.HTTPConnection("api.thingspeak.com:80")
    conn.request("POST", "/update", params, headers)
    response = conn.getresponse()
    print response.status, response.reason
    data = response.read()
    conn.close()
 
#sleep for 16 seconds (api limit of 15 secs)
if __name__ == "__main__":
    while True:
        doit()
        time.sleep(16) 