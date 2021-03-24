from flask import Flask, render_template
from flask import request
import grpc

from compute_pb2 import NameRequest
from compute_pb2 import ImageRequest
from compute_pb2 import CapacityRequest
from compute_pb2_grpc import ComputeServiceStub

from database_pb2 import DbListRequest
from database_pb2 import DbAuthRequest
from database_pb2 import DbUploadRequest
from database_pb2_grpc import DatabaseServiceStub

# from clients.billing_pb2 import DbListRequest
# from clients.billing_pb2_grpc import DatabaseServiceStub

# initialise the app
app = Flask(__name__)

# initialise the gRPC channels
compute_channel = grpc.insecure_channel("localhost:2410")
database_channel = grpc.insecure_channel("localhost:2411")
# billing_channel = grpc.insecure_channel("localhost:2412")

# create the gRPC clients
compute_client = ComputeServiceStub(compute_channel)
database_client = DatabaseServiceStub(database_channel)


# billing_client = ComputeServiceStub(billing_channel)


@app.route('/')
def index():
    return render_template("index.html")


@app.route("/compute.html", methods=['GET'])
def compute():
    resp = []
    if request.url.find('selected_name') != -1:
        name_request = NameRequest(name=request.args.get('selected_name'))
        name_response = compute_client.validateName(name_request)
        resp.append(name_response)
    elif request.url.find('selected_region') != -1:
        image_request = ImageRequest(regionName=request.args.get('selected_region'))
        image_response = compute_client.retrieveImages(image_request)
        respy = image_response
        for r in respy:
            resp.append("Image: %s && Response Message: %s" % (r.imageName, r.responseMessage))
    elif request.url.find('cpu_count') != -1:
        capacity_request = CapacityRequest(
            region=request.args.get('region'),
            cpuCount=int(request.args.get('cpu_count'))
        )
        capacity_response = compute_client.validateCapacity(capacity_request)
        resp.append(capacity_response)
    else:
        resp.append("No results")

    # resp = name_response.responseMessage

    return render_template(
        "compute.html",
        output=resp
    )


@app.route("/database.html", methods=['GET'])
def database():
    resp = []

    if request.url.find('region') != -1:
        list_request = DbListRequest(name=request.args.get('region'))
        list_response = database_client.listDatabases(list_request)
        respy = list_response
        for r in respy:
            resp.append("Database Name: %s :: Response Message: %s :: Response Code: %s" % (
                r.databaseName, r.responseMessage, r.responseCode))

    elif request.url.find('userName') != -1:
        auth_request = DbAuthRequest(
            databaseName=request.args.get('databaseName'),
            userName=request.args.get('userName'),
            password=request.args.get('password')
        )
        auth_response = database_client.databaseLogin(auth_request)
        resp.append("Database Token: %s :: Response Message: %s :: Response Code: %s" % (
                auth_response.databaseToken, auth_request.responseMessage, auth_response.responseCode))

    elif request.url.find('token') != -1:
        upload_request = DbUploadRequest(
            databaseName=request.args.get('databaseName'),
            databaseToken=request.args.get('databaseToken'),
            payloadChunk=request.args.get('payloadChunk')
        )
        upload_response = database_client.databaseUpload(upload_request)
        resp.append("Response Message: %s :: Response Code: %s" % (
                upload_response.responseMessage, upload_response.responseCode))

    else:
        resp.append("No results")

    return render_template(
        "database.html",
        output=resp
    )


@app.route("/billing.html", methods=['GET'])
def billing():
    return render_template("billing.html")
