import { PageRequest } from "../sharedComponents/DisplayTable"

function filterOrderToAPI(order: FilterOrder){
    switch(order){
        case "<":
            return "<";
        case ">":
            return ">";
        case "=":
            return ":";
        default:
            console.error("Unmanaged filter order type.")
    }
}

export type FilterOrder = "=" | ">" | "<"

export type TableSortType = "desc" | "asc"

export type SearchFilter = {filterField: string, filterValue: string, filterOrder?: FilterOrder }

export type SearchSort = {field: string, order: TableSortType }

export type SearchElementsParameters = {
    size: number,
    page: number,
    filters?: SearchFilter[],
    sort?: SearchSort,
}

export async function searchElements<T>(path: string, params?: SearchElementsParameters): Promise<PageRequest<T> | undefined> {
    let formattedRequest = path
    if(params){
        formattedRequest += `?size=${params.size}`
        formattedRequest += `&page=${params.page}`
        if(params.filters){
            formattedRequest += "&request=" + encodeURI(params.filters.map(filter => {
                let separator = filterOrderToAPI(filter.filterOrder ? filter.filterOrder : "=")
                return `${filter.filterField}${separator}\`${filter.filterValue}\``
            }).join(","))
        }
        if(params.sort){
            formattedRequest += `&sortBy=${params.sort.field}&order=${params.sort.order}`
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
