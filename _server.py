

from flask import Flask, jsonify, abort, request
import os
import socket
import sys
import json
import base64
from threading import Thread

app = Flask(__name__)
@app.route('/')
@app.route('/kidsbus',methods=['GET'])
def getKidsbus():
    return 'Hello Kidbus',200


#Main
if __name__ == '__main__':

    app.run(host='155.230.118.252',port=5001,debug=True)

