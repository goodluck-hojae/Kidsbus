import os
import sys
from sqlalchemy import Column, ForeignKey, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine

Base = declarative_base()

class Parent(Base):
    __tablename__ = 'parent'
    id = Column(Integer, primary_key=True)
    name = Column(String(20), nullable=False)
    account = Column(String(20), unique=True, nullable=False)
    password = Column(String(20), nullable=False)
    birth_date = Column(String(20))
    phone_number = Column(String(20), nullable=False)

class Location(Base):
    __tablename__ = 'location'
    id = Column(Integer, primary_key=True)
    name = Column(String(20), unique=True, nullable=False)
    latitude = Column(String(20), nullable=False)
    longitude = Column(String(20), nullable=False)

class Child(Base):
    __tablename__ = 'child'
    id = Column(Integer, primary_key=True)
    name = Column(String(20), nullable=False)
    gender = Column(String(1), nullable=False)
    birth_date = Column(String(20), nullable=False)
    parent_id = Column(Integer, ForeignKey('parent.id'))
    parent = relationship(Parent)
    location_id = Column(Integer, ForeignKey('location.id'))
    location = relationship(Location)

class Attentance(Base):
    __tablename__ = 'attendance'
    id = Column(Integer, primary_key=True)
    date = Column(String(20),nullable=False)
    is_attended = Column(String(1),nullable=False)
    child_id = Column(Integer, ForeignKey('child.id'))
    child = relationship(Child)

# Create an engine that stores data in the local directory's
# sqlalchemy_example.db file.
engine = create_engine('sqlite:///kidsbus_database.db')
Base.metadata.create_all(engine)