from database_declarative import Child, Base, Location, Parent
from sqlalchemy import create_engine
engine = create_engine('sqlite:///kidsbus_database.db')
Base.metadata.bind = engine
from sqlalchemy.orm import sessionmaker
DBSession = sessionmaker()
DBSession.bind = engine
session = DBSession()
# Make a query to find all Persons in the database
session.query(Parent).all()

parent = session.query(Parent).filter(Parent.account == "19921116").first()
print(parent.account)
 # Return the first Person from all Persons in the database
person = session.query(Parent).first()
print(person.name)

# Find all Address whose person field is pointing to the person object
location = session.query(Location).first()
print(location.latitude)

# Retrieve one Address whose person field is point to the person object
#session.query(Child).filter(#Address.person == person).one()
child = session.query(Child).first()
print(child.name)