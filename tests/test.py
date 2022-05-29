from fastapi import FastAPI
from fastapi.testclient import TestClient

# importing sys
import sys
sys.path.insert(0, '../dev')

from venture_registration import app

client = TestClient(app)

def test_read_main():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"msg": "Hello Fractional Investment Service"}
