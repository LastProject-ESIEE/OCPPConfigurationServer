
// Status type definition
export type Status = {
    error: string,
    state: boolean, // true: connected, false: disconnected
    step: 'FIRMWARE' | 'CONFIGURATION',
    status: "PENDING" | "PROCESSING" | "FINISHED" | "FAILED"
}

// ChargePoint type definition
export type ChargePoint = {
    id: number,
    name: string,
    description: string,
    status: Status,
    firmware: string
}

export async function getChargePointList(): Promise<ChargePoint[]> {
    let request = await fetch(window.location.origin + "/chargepoint/all")
    if(request.ok){
        let content = await request.json()
        let configurationList = (content as ChargePoint[])
        if(configurationList != null){
          console.log(configurationList)
          return configurationList
        }else{
            console.log("Fetch configuration list failed " + content)
        }
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
    }
    return []
}