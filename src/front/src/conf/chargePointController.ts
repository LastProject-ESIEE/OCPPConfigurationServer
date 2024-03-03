import { PageRequest } from "../sharedComponents/DisplayTable"

// Websocket notification for charge point status update
export type WebSocketChargePointNotification = {
    id: number,
    status: ChargePointStatus,
}

// Status type definition
export type ChargePointStatus = {
    error: string,
    state: boolean, // true: connected, false: disconnected
    step: 'FIRMWARE' | 'CONFIGURATION',
    status: "PENDING" | "PROCESSING" | "FINISHED" | "FAILED"
    lastUpdate: Date
}

// ChargePoint type definition
export type ChargePoint = {
    id: number,
    serialNumberChargepoint: string,
    type: string,
    constructor: string,
    clientId: string,
    configuration: any,
    status: ChargePointStatus,
}


export async function searchChargePoint(
        size: number = 10,
        page: number = 0,
        filter?: {filterField: string, filterValue: string },
        sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<ChargePoint> | undefined> {
    
    let request = await fetch(window.location.origin + `/api/chargepoint/search?size=${size}&page=${page}`)
    if(request.ok){
        let content = await request.json()
        let chargePoint = (content as PageRequest<ChargePoint>)
        if(chargePoint != null){
          console.log(chargePoint)
          return chargePoint
        }else{
            console.log("Fetch charge point page failed " + content)
        }
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
    }
    return undefined
}

export type CreateChargepointDto = {
    serialNumber : string,
    type: string,
    constructor: string,
    clientId: string,
    configuration: number,
}


export async function postNewChargepoint(chargepoint: CreateChargepointDto): Promise<boolean> {

    let request = await fetch("/api/chargepoint/create",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(chargepoint)
        })
    if (request.ok) {
        return true
    } else {
        console.error("Couldn't save chargepoint, error code: " + request.status)
        return false
    }
}