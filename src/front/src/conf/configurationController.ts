
export type ConfigurationView = {
    id: number,
    name: string,
    description: string,
    timestamp: string
}

export async function getConfigurationList(): Promise<ConfigurationView[]> {
    let request = await fetch(window.location.origin + "/api/configuration/all")
    if(request.ok){
        let content = await request.json()
        let configurationList = (content as ConfigurationView[])
        if(configurationList != null){
          return configurationList
        }else{
            console.log("Fetch configuration list failed " + content)
        }
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
    }
    return []
}