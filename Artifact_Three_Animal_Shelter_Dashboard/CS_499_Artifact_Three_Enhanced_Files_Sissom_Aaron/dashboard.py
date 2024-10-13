#! /usr/bin/env python
# -*- coding: utf-8 -*-

"""
The Animal Shelter Search Dashboard is a user-friendly web application designed to
streamline the process of searching for adoptable pets in animal shelters.
Built with Dash and powered by a MongoDB backend, this interactive dashboard allows
users to filter and visualize data on various animals available for adoption. 

Function definitions: 
   - update_dashboard()
        - Responsible for updating the main content of the dashboard based on
        user interactions, such as selections made from dropdown menus or button clicks.
        
   - update_style()
        - Manages the visual styling of various components within the dashboard,
        particularly those that respond to user interactions.
        
   - update_graph()
        - Dedicated to updating graphical representations of data, such as pie charts,
        bar charts, or other visualizations within the dashboard.
        
   - update_map()
        - Focuses on updating any geographical visualizations, such as maps
        that may display locations of animal shelters or adoption centers.

"""

__author__ = "Aaron Sissom"
__course__ = "SNHU CS499 Capstone"
__instructor__ = "Prof. Brooke Goggin"
__version__ = "0.0.5"
__email__ = "aaron.sissom@snhu.edu"


#############
#  IMPORTS  #
#############

from jupyter_dash import JupyterDash

import dash
from dash import Dash, html, dcc, callback, Output, Input, State
from dash import dash_table as dt
import dash_leaflet as dl
import plotly.express as px
from dash.dash_table.Format import Group

import os
import numpy as np
import pandas as pd
from pymongo import MongoClient
from bson.json_util import dumps

from database_functions import AnimalShelter
import base64

######################################
# USER AUTHENTICATION/ DB CONNECTION #
######################################

username = "aacuser"
password = "CS340"
dbname = "AAC"

# Connect to database via CRUD Module
shelter = AnimalShelter(username, password, dbname)

# Class read method must support return of list object and accept projection json input
# sending the read method an empty document requests all documents be returned
df = pd.DataFrame.from_records(shelter.read({}))

###########################
# DASHBOARD LAYOUT / VIEW #
###########################

app = Dash('Grazioso Salvare Search for Rescue Animals Web App')

image_filename = 'Grazioso_Salvare_Logo.png'
encoded_image = base64.b64encode(open(image_filename, 'rb').read())

app.layout = html.Div([
    html.Div(id='hidden-div', style={'display':'none'}),
    html.Center([
        html.A([
                html.Img(id='customer-image',
                     src='data:image/png;base64,{}'.format(encoded_image.decode()),
                     alt='Grazioso Salvare Logo',
                     style={'width': 225})
            ], href="http://127.0.0.1/8050", target="_blank"),
        html.H1("Animal Shelter Search Dashboard"),
        html.H5("Developed by Aaron Sissom", style={'color':'black'})
    ]),
    html.Hr(),

    # Add in code for filtering options (radio buttons, dropdown menus, etc.)
    html.Div(className='row',
             style={'style':'flex'},
             children=[
                 html.Span("Filter by: ", style={'margin':6}),
                 html.Span(
                     html.Button(id='submit-button-one', n_clicks=0, children='Cats'),
                     style={'margin':6}),
                 html.Span(
                     html.Button(id='submit-button-two', n_clicks=0, children='Dogs'),
                     style={'margin':6}),
                 html.Span(
                     html.Button(id='reset-button', n_clicks=0, children='Reset',
                     style={'background-color':'red', 'color':'white'}),
                     style={'margin':6}),
                 html.Span("or", style={'margin':6}),
                 html.Span([
                     dcc.Dropdown(
                         id='filter-type',
                         options=[
                             {'label':'Water Rescue', 'value':'WR'},
                             {'label':'Mountain or Wilderness Rescue', 'value':'MWR'},
                             {'label':'Disaster Rescue or Individual Tracking', 'value':'DRIT'},
                         ],

                         placeholder="Select a dog category filter",
                         style={'marginLeft':5, 'width':350}
                     )
                 ])
             ]
         ),
    html.Hr(),
    dt.DataTable(
        id='datatable-id',
        columns=[
            {'name': i, 'id': i, 'deletable': False, 'selectable': True} for i in df.columns
        ],
        data=df.to_dict('records'),

        # Add in code for interactive data tables
        editable = False,
        filter_action = "native",
        sort_action = "native",
        sort_mode = "multi",
        column_selectable = False,
        row_selectable = False,
        row_deletable = False,
        selected_columns = [],
        selected_rows = [0],
        page_action = "native",
        page_current = 0,
        page_size = 10
        
    ),
    html.Br(),
    html.Hr(),

    # Add code to set up side-by-side display of geolocation chart and graph
    html.Div(className='row',
             style={'display':'flex'},
             children=[
                 html.Div(
                             id='graph-id',
                             className='col s12 m6',
                         ),
                 html.Div(
                             id='map-id',
                             className='col s12 m6',
                         )
                     ]
            ),

    # Add code for unique identifier (Name, Date, etc.) in the page footer
    html.Div([
        html.Hr(),
        html.P([
                "Module 5 Artifact Two Enhancement Submission - Prof. Brooke Goggin",
                html.Br(),
                "SNHU CS-499 Computer Science Capstone - September 30, 2024"
                ], style={'fontSize': 12})
            ])
        ])


###############################################
# Interaction Between Components / Controller #
###############################################

@app.callback(
    Output('datatable-id', 'data'),
    [Input('filter-type', 'value'),
     Input('submit-button-one', 'n_clicks'),
     Input('submit-button-two', 'n_clicks')]
)

def update_dashboard(selected_filter, button1, button2):
    
    if (selected_filter == 'DRIT'):
        df = pd.DataFrame(list(shelter.read(
            {
                "animal_type":"Dog",
                "breed":{"$in":["Doberman Pinsch", "German Shepherd", "Golden Retriever", "Pit Bull", "Rottweiler"]},
                "sex_upon_outcome":"Intact Male",
                "age_upon_outcome_in_weeks":{"$gte":20},
                "age_upon_outcome_in_weeks":{"$lte":300}
            }
        )))
        
    elif (selected_filter == 'MWR'):
        df = pd.DataFrame(list(shelter.read(
            {
                "animal_type":"Dog",
                "breed":{"$in":["German Shepherd", "Alaskan Malamute", "Old English Sheepdog", "Siberian Husky Mix", "Rottweiler"]},
                "sex_upon_outcome":"Intact Male",
                "age_upon_outcome_in_weeks":{"$gte":26},
                "age_upon_outcome_in_weeks":{"$lte":156}

            }
        )))
    
    elif (selected_filter == 'WR'):
        df = pd.DataFrame(list(shelter.read(
            {
                "animal_type":"Dog",
                "breed":{"$in":["Labrador Retriever Mix", "Chesa Bay Retr", "Newfoundland Mix"]},
                "sex_upon_outcome":"Intact Female",
                "age_upon_outcome_in_weeks":{"$gte":26},
                "age_upon_outcome_in_weeks":{"$lte":156}
            }
        )))

    # Add code to cycle through filter
    elif (int(button1) > int(button2)):
        df = pd.DataFrame(list(shelter.read({"animal_type":"Cat"})))
    elif (int(button2) > int(button1)):
        df = pd.DataFrame(list(shelter.read({"animal_type":"Dog"})))
    else:
        df = pd.DataFrame.from_records(shelter.read({}))

    data = df.to_dict('records')

    return data

@app.callback(
    [Output('submit-button-one', 'n_clicks'),
     Output('submit-button-two', 'n_clicks')],
    [Input('reset-button', 'n_clicks')]
)

# function resets the number of button clicks on the Cat/Dog filter
def update(reset):
    return 0, 0

@app.callback(
    Output('datatable-id', 'style_data_conditional'),
    [Input('datatable-id', 'selected_columns'),
     Input('datatable-id', 'derived_viewport_selected_rows'),
     Input('datatable-id', 'active_cell')]
)

# highlights a column or row when selected on the page
def update_style(selected_columns, selected_rows, active_cell):
    
    if active_cell is not None:
        style = [{
                    'if': {'row_index' : active_cell['row']},
                    'background_color': '#A5D6A7'
                }]
    else:
        style = [{
                    'if': {'row_index':i},
                    'background_color': '#A5D6A7'
                } for i in selected_columns]
    return (style +
                [{
                    'if': {'column_id': i},
                    'background_color': '#80DEEA'
                } for i in selected_columns]
            )

########################
# RENDER CHART DISPLAY #
########################
 
@app.callback(
    Output('graph-id', 'children'),
    [Input('datatable-id', 'derived_viewport_data')]
)

# add a pie chart that will display breed percentages based on selected animal
def update_graph(viewData):
    
    # Converts the view data to a DataFrame
    dff = pd.DataFrame.from_dict(viewData)
    
    # Check if DataFrame has data
    if dff.empty or 'breed' not in dff.columns:
        # If no data or no 'breed' column, return an empty graph or message
        return [html.P("No data available to display")]

    # Custom pie chart colors
    custom_colors = ['#ff9999','#66b3ff','#99ff99','#ffcc99','#c2c2f0']
    
    # Create graph
    fig = px.pie(
        dff,
        names = 'breed',
        title = 'Animal Breed %',
        color_discrete_sequence = custom_colors
    )

    return [dcc.Graph(figure = fig)]

######################
# RENDER MAP DISPLAY #
######################

@app.callback(
    Output('map-id', 'children'),
    [Input('datatable-id', 'derived_viewport_data'),
     Input('datatable-id', 'selected_rows'),
     Input('datatable-id', 'active_cell')]
)

# adds a map window that will display animal geolocation data
def update_map(viewData, selected_rows, active_cell):
        
    # Checking viewData exists
    if viewData is None or len(viewData) == 0:
        return "No data available to display"

    # Convert view data into a DataFrame
    dff = pd.DataFrame.from_dict(viewData)

    # Handling the active cell or selected row
    if active_cell is not None:
        row = active_cell['row']
    elif selected_rows is not None and len(selected_rows) > 0:
        row = selected_rows[0]
    else:
        row = 0

    # Retrieving data for the selected row
    lat = dff.loc[row, 'location_lat']
    long = dff.loc[row, 'location_long']
    name = dff.loc[row, 'name']
    breed = dff.loc[row, 'breed']
    animal = dff.loc[row, 'animal_type']
    age = dff.loc[row, 'age_upon_outcome']

    if name == "":
        name = "No name available"

    return [
        dl.Map(
            style={'width':'1000px', 'height':'500px'},
            center=[lat, long], zoom=10,
            children=[
                dl.TileLayer(id="base-layer-id"),
                # Add in a marker with tooltip
                dl.Marker(
                    position=[lat,long],
                    children=[
                        dl.Tooltip("({:.3f}, {:.3f})".format(lat, long)),
                        dl.Popup([
                            html.H2(name),
                            html.P([
                                html.Strong("{} | {} | Age: {}".format(name, animal, age)),
                                html.Br(),
                                breed])
                            ])
                        ]
                    )
                ]
            )
        ]

#########################
# APPLICATION EXECUTION #
#########################

# Main method
if __name__ == '__main__':
    app.run()