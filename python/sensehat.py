import httplib, urllib
import time

from sense_hat import SenseHat
sense = SenseHat()
sense.clear()
 
def doit():
    
    temp = sense.get_temperature()
    # temp = round(temp,2)
    humidity = sense.get_humidity()
    # humidity = round(humidity, 2)
    pressure = sense.get_pressure()
    # pressure = round(pressure, 2)
    print("temperature: %s, humidity: %s, pressure: %s" % temp, humidity, pressure)
    
    params = urllib.urlencode({'field1': temp, 'field2': humidity, 'field3': pressure ,'key':'VXJ2HONUS8YNTP0G'})
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