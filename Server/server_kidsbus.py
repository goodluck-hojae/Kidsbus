from flask import Flask, jsonify, abort, request
from database_declarative import Child, Base, Location, Parent, Attentance
from sqlalchemy import create_engine
engine = create_engine('sqlite:///kidsbus_database.db')
Base.metadata.bind = engine
from sqlalchemy.orm import sessionmaker
DBSession = sessionmaker()
DBSession.bind = engine
import json
app = Flask(__name__)
session = DBSession()
@app.route('/')
@app.route('/kidsbus/',methods=['GET'])
def hello():
    return "kidsbus",200

'''''''''''''''''''''''''''''''''''''''''''''''''''''''GET'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
# Get Parent Id
@app.route('/kidsbus/get_parent_id_by_name/<name>',methods=['GET'])
def get_parent_id(name):
    parent = session.query(Parent).filter(Parent.name == name).first()
    return  json.dumps({'parent_id':parent.id}, ensure_ascii=False)

# Get Child Id
@app.route('/kidsbus/get_child_id_by_name/<name>',methods=['GET'])
def get_child_id(name):
    child = session.query(Child).filter(Child.name == name).first()
    return  json.dumps({'child_id':child.id}, ensure_ascii=False)

# Get Location Id
@app.route('/kidsbus/get_location_id_by_name/<name>',methods=['GET'])
def get_location_id(name):
    location = session.query(Location).filter(Location.name == name).first()
    return  json.dumps({'location_id':location.id}, ensure_ascii=False)

# Get Attendance Id
@app.route('/kidsbus/get_attendance_id_by_child_id/<int:id>',methods=['GET'])
def get_attendance_id(id):
    attendacne = session.query(Attentance).filter(Attentance.child_id == id).first()
    return  json.dumps({'attendance_id':attendacne.id}, ensure_ascii=False)
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
# Get Parent Id
@app.route('/kidsbus/get_parent_info_by_id/<int:id>',methods=['GET'])
def get_parent_info_by_id(id):
    parent = session.query(Parent).filter(Parent.id == id).first()
    return json.dumps({'parent_id':parent.id,
                       'parent_name':parent.name,
                       'parent_account':parent.account,
                       'parent_password':parent.password,
                       'parent_birth_date':parent.birth_date,
                       'parent_phone_number':parent.phone_number,
                       'parent_location_id': parent.location_id}, ensure_ascii=False)


# Get Child Id
@app.route('/kidsbus/get_child_info_by_id/<int:id>',methods=['GET'])
def get_child_info_by_id(id):
    child = session.query(Child).filter(Child.id == id).first()
    return json.dumps({'child_id': child.id,
                       'child_name': child.name,
                       'child_gender': child.gender,
                       'child_birth_date':  child.birth_date,
                       'child_parent_id': child.parent_id} , ensure_ascii=False)
# Get Location Id
@app.route('/kidsbus/get_location_info_by_id/<int:id>',methods=['GET'])
def get_location_info_by_id(id):
    location = session.query(Location).filter(Location.id == id).first()
    return  json.dumps({'location_id': location.id,
                       'location_name': location.name,
                       'location_latitude': location.latitude,
                       'location_longitude':  location.longitude, } , ensure_ascii=False)

# Get Attendance Id
@app.route('/kidsbus/get_attendance_info_by_id/<int:id>',methods=['GET'])
def get_attendance_info_by_id(id):
    attendacne = session.query(Attentance).filter(Attentance.id == id).first()
    return json.dumps({'attendacne_id': attendacne.id,
                       'attendacne_date': attendacne.date,
                       'attendacne_is_attended': attendacne.is_attended,
                       'attendacne_child_id': attendacne.child_id }, ensure_ascii=False)

'''''''''''''''''''''''''''''''''''''''''''''''''''''''POST'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
# Register Parent
@app.route('/kidsbus/post/register_parent', methods=['POST'])
def register_parent():
    #GET REQUEST
    parent_json = request.json

    #DASTBASE OBJECT
    new_parent = Parent(name=parent_json['parent']['name'],
                        account = parent_json['parent']['account'],
                        password = parent_json['parent']['password'],
                        birth_date = parent_json['parent']['birth_date'],
                        phone_number = parent_json['parent']['phone_number'],
                        location_id = parent_json['parent']['location_id'])

    session.add(new_parent)
    session.commit()

    #JSON
    #parent_info = session.query(Parent).filter(Parent.id == parent_json['parent']['id']).one()
    parent = {
        "parent": {
            "name": parent_json['parent']['name'],
            "account": parent_json['parent']['account'],
            "password": parent_json['parent']['password'],
            'birth_date': parent_json['parent']['birth_date'],
            'phone_number': parent_json['parent']['phone_number'],
            'location_id':  parent_json['parent']['location_id']
        }
    }
    return  json.dumps(parent, ensure_ascii=False) , 200

# Register Location
@app.route('/kidsbus/post/register_location', methods=['POST'])
def register_location():
    location_json = request.json

    #DASTBASE OBJECT
    new_location = Location(name=location_json['location']['name'],
                            latitude=location_json['location']['latitude'],
                            longitude=location_json['location']['longitude'])

    session.add(new_location)
    session.commit()

    # JSON
    location = {
        "location": {
            "name": location_json['location']['name'],
            "latitude": location_json['location']['latitude'],
            "longitude": location_json['location']['longitude']
        }
    }

    return  json.dumps(location, ensure_ascii=False) , 200

# Register Attendance
@app.route('/kidsbus/post/register_attendance', methods=['POST'])
def register_attendance():
    attendance_json = request.json
    #DASTBASE OBJECT
    #attendance = session.quest(Attentance).filter(Attentance.id == attendance_json['attendance']['id'])
    new_attendance = Attentance(date=attendance_json['attendance']['date'],
                                child_id=attendance_json['attendance']['child_id'],
                               is_attended = attendance_json['attendance']['is_attended'])

    session.add(new_attendance)
    session.commit()

    # JSON
    attendance = {
        "attendance": {
            "date": attendance_json['attendance']['date'],
            "child_id": attendance_json['attendance']['child_id'],
            "is_attended":attendance_json['attendance']['is_attended']
        }
    }

    return  json.dumps(attendance, ensure_ascii=False) , 200

# Register Child(parent & location information needed)
@app.route('/kidsbus/post/register_child', methods=['POST'])
def register_child():
    child_json = request.json

    print(child_json)
    #DASTBASE OBJECT
    #parent = session.query(Parent).filter(Parent.id == child_json['child']['parent_id'])
    new_child = Child(name = child_json['child']['name'],
                      gender= child_json['child']['gender'],
                      birth_date = child_json['child']['birth_date'],
                      parent_id = child_json['child']['parent_id'] )
    session.add(new_child)
    session.commit()

    # JSON
    #child_info = session.query().filter(Child.id == child_json['child']['id']).one()
    child = {
        "child": {
            "name": child_json['child']['name'],
            "gender": child_json['child']['gender'],
            "birth_date": child_json['child']['birth_date'],
            "parent_id": child_json['child']['parent_id']
        }
    }

    return json.dumps(child, ensure_ascii=False), 200

''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

#Main
if __name__ == '__main__':
    app.run(host='155.230.118.252',port=5001,debug = True)
