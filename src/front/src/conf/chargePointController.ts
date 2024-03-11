import { Configuration } from "./configurationController";

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
    configuration: Configuration,
    status: ChargePointStatus,
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

export async function updateChargepoint(id: number, chargepoint: CreateChargepointDto) {
    let request = await fetch(`/api/chargepoint/${id}`,
        {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(chargepoint)
        })
    if (request.ok) {
        return true
    } else {
        console.error("Couldn't update chargepoint, error code: " + request.status)
        return false
    }
}

export async function getChargepointById(id: number) {
    let request = await fetch(`/api/chargepoint/${id}`)
    if(request.ok){
        let content = await request.json()
        let chargePoint = (content as ChargePoint)
        if(chargePoint != null){
            return chargePoint
        }else{
            console.error("Fetch chargepoint failed " + content)
        }
    }else{
        console.error("Fetch chargepoint failed, error code:" +  request.status)
    }
    return undefined
}