# For Fractional Investment Service Platform
# Venture Portal
# Registration of venture / enterprise entity with required details

from typing import Union

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()


@app.get("/")
def read_root():
    	return {"Hello": "World"}

class Venture(BaseModel):
    	name: str
    	typeOfVenture: str
    	description: Union[ str, None] = None
#"Default Venture Description"
    	
class Project(BaseModel):
	venture_name: str
	project_name: str
	location: str
	city: str
	pincode: int
	project_value_size: int
	project_scope_size: int
	description: Union[ str, None ]= None
#"Default Project Description"
    	

@app.get("/fractionalInvestmentService/venture/{venture_unique_name}")
def read_item(venture_unique_name: str, q: Union[str, None] = None):
    	return {"venture_unique_name": venture_unique_name, "q": q}	
    	
@app.put("/fractionalInvestmentService/venture/{venture_unique_name}")
def update_item(venture_unique_name: str, venture: Venture):
	return {"venture_description": venture.description, "venture_unique_name": venture.name, "venture_type" : venture.typeOfVenture}
    
    
