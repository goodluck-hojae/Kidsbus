import json
import requests
import asyncore
import socket
import sys
import base64
import urllib
import os
i = 1

def get(base,query):
    print(base)
    print(query)
    print(base+urllib.parse.quote(query,safe='')) 
    r = requests.get(base+urllib.parse.quote(query,safe='')) 
    try:
      result = json.dumps(r.json()) 
    except json.decoder.JSONDecodeError:
      result = "GET Error" 
    return str(r.status_code) + '\n' + result   
#print(get('http://127.0.0.1:5001/','DocumentManifest/Patient/1?_format=json'))

def post(address,jsonFormatString): 
    print(address + " " + jsonFormatString)
    r = requests.post(address, json = json.loads(jsonFormatString))
    try:
      result = "Post Success\n"+json.dumps(r.json())
    except :
      result = "Post Error"
    return result  