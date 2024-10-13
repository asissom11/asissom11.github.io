#! /usr/bin/env python
# -*- coding: utf-8 -*-

"""
Purpose: The 'AnimalShelter' class is designed to encapsulate all necessary operations for
    managing animal data within a MongoDB database for an animal shelter application. It provides
    a structured way to perform CRUD (Create, Read, Update, Delete) operations on the database. 

Function definitions: 
    - __init__()
        - Parameters:
            username: used for database authentication
            password: used for database authentication
            dbname: name of target database for connection
        - Initializes the MongoDB client using the provided username, password, and database name.
        Establishes a connection to the specified database and sets up the animals collection,
        making it ready for data manipulation.
        
    - create()
        - Parameters:
            List of dictionaries representing animal data
        - Inserts animal data into database using the 'insert_many()' function call.
        
    - read()
        - Parameters:
            Dictionary of one or more database queries to filter results
        - Utilizes the 'find()' function call to return matching records from the database without
        including the '_id' field.
        
    - update()
        - Parameters:
            Dictionary of one or more database queries to identify records to update, along with new data
        - Uses the 'update_many()' function call to apply changes to all matching records.
        
    - delete()
        - Parameters:
            Dictionary of one or more database queries to specify which records to delete
        - Calls the 'delete_many()' function to remove all specified records that match the given criteria.
"""

__author__ = "Aaron Sissom"
__course__ = "SNHU CS499 Capstone"
__instructor__ = "Prof. Brooke Goggin"
__version__ = "0.0.5"
__email__ = "aaron.sissom@snhu.edu"

from pymongo import MongoClient
from bson.objectid import ObjectId
from pprint import pprint

# This class will house all of the CRUD operations to manipulate the data in the database
class AnimalShelter(object):

    def __init__(self, username, password, dbname):
        # Change port number to localhost
        #port = 27017

        # Initializing MongoClient to access databases and collections
        self.client = MongoClient(
            'mongodb://%s:%s@localhost/%s' % (username, password, dbname))

        self.database = self.client[dbname]
        collname = 'animals'
        self.collection = self.database[collname]

# In all of the below methods, the data type for 'data' should be a list of at least one dictionary.

    # 'C' in CRUD
    def create(self, data):
        if data is not None:
            return self.collection.insert_many(data)
        else:
            raise Exception("ERROR: Empty field. Nothing to save.")

    # 'R' in CRUD
    def read(self, data):
        if data is not None:
            return self.collection.find(data, {"_id": False})
        else:
            raise Exception("ERROR: Empty field. Nothing to save.")

    # 'U' in CRUD
    def update(self, data):
        if data is not None:
            return self.collection.update_many(data)
        else:
            raise Exception("ERROR: Empty field. Nothing to save.")

    # 'D' in CRUD
    def delete(self, data):
        if data is not None:
            return self.collection.delete_many(data)
        else:
            raise Exception("ERROR: Empty field. Nothing to save.")
