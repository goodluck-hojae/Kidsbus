from flask import Flask, jsonify, abort, request
from database_declarative import Child, Base, Location, Parent
from sqlalchemy import create_engine
engine = create_engine('sqlite:///kidsbus_database.db')
Base.metadata.bind = engine
from sqlalchemy.orm import sessionmaker
DBSession = sessionmaker()
DBSession.bind = engine
import json
app = Flask(__name__)
session = DBSession()

#GET Action
@app.route('/')
@app.route('/kidsbus/get/parent_name/<name>',methods=['GET'])
def get_parent_name(name):
    parent = session.query(Parent).filter(Parent.name == name).one()
    return  json.dumps({'parent_name':parent.name}, ensure_ascii=False)

#POST Action
@app.route('/kidsbus/post',methods=['POST'])
def postPatient():
    print(request.json);
    return "POST HELLO KIDSBUS",201

#Main
if __name__ == '__main__':
    app.run(host='localhost',port=5001,debug=True)
