import { PageRequest } from "../sharedComponents/DisplayTable"

export type SearchFilter = {filterField: string, filterValue: string }

export type SearchElementsParameters = {
    size: number,
    page: number,
    filters?: SearchFilter[],
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }
}

export async function searchElements<T>(path: string, params?: SearchElementsParameters ): Promise<PageRequest<T> | undefined> {
    let formattedRequest = path
    if(params){
        formattedRequest += `?size=${params.size}`
        formattedRequest += `&page=${params.page}`
        if(params.filters){
            formattedRequest += "&request=" + encodeURI(params.filters.map(filter => {
                return `${filter.filterField}:\`${filter.filterValue}\``
            }).join(","))
        }
    }
    let request = await fetch(formattedRequest)
    if(request.ok){
        let content = await request.json()
        let response = (content as PageRequest<T>)
        if(response){
            console.log(response)
            return response
        }else{
            console.log("Fetch page failed " + content)
        }
    }else{
        console.log("Fetch list failed, error code:" +  request.status)
    }
    return undefined
}
