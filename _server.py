from flask import Flask, jsonify, abort, request
import os
import socket
import sys
import json
import base64
from threading import Thread

app = Flask(__name__)

#GET Action
@app.route('/')
@app.route('/kidsbus/get',methods=['GET'])
def getKidsbus():

    return jsonify({'test':"GET HELLO KIDSBUS"}),201

#POST Action
@app.route('/kidsbus/post',methods=['POST'])
def postPatient():
    print(request.json);
    return "POST HELLO KIDSBUS",201


#Main
if __name__ == '__main__':
    app.run(host='0.0.0.0',port=5001,debug=True)

