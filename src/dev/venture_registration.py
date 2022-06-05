# Copyright not assigned. license to be finalized
#
# For Fractional Investment Service Platform
# Venture Portal
# Registration of venture / enterprise entity with required details

from typing import Union

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()


ventureMap = {}
ventureMap["test2"]= 123; 

@app.get("/")
def read_root():
    	return {"msg": "Hello Fractional Investment Service"}

class Venture(BaseModel):
    	name: str
    	typeOfVenture: str = None
    	description: Union[ str, None] = None
    	address_city: str = None
    	country_region: str = None
    	zip_code: str = None
    	projects = []
#"Default Venture Description"

class Project(BaseModel):
	venture_name: str
	project_name: str
	location: str = None
	city: str = None
	pincode: int = None
	project_value_size: int = None
	project_scope_size: int = None
	project_start_date: str = None
	project_asset_dep_date: str = None
	project_financials: str = None
	description: Union[ str, None ]= None
#"Default Project Description"
    	

@app.get("/fractionalInvestmentService/venture/{venture_unique_name}")
def read_item(venture_unique_name: str, q: Union[str, None] = None):
	venture = ventureMap.get(venture_unique_name)
	if(venture == None):
		venture = getDefaultVenture()
	return {"venture_unique_name": venture.name, "type": venture.typeOfVenture, "desc" : venture.description, "zip" : venture.zip_code, "projects" : venture.projects}	
    	
@app.post("/fractionalInvestmentService/venture/{venture_unique_name}")
def create_venture(venture_unique_name: str, venture: Venture):
	ventureMap[venture_unique_name] = venture
	return {"venture_description": venture.description, "venture_unique_name": venture.name, "venture_type" : venture.typeOfVenture}

@app.post("/fractionalInvestmentService/project/{project_name}")
def add_project( project: Project):
	venture = ventureMap[project.venture_name]
	venture.projects.append(project)
	return {"project_name": project.project_name, "venture_name": venture.name}

def getDefaultVenture():
	venture = Venture(name="Default")
	project = Project(venture_name="Default", project_name="default_project")
	venture.projects.append(project)
	return venture
