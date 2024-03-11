
// TechnicalLog type definition
export type TechnicalLog = {
    id: number,
    date: Date,
    component: 'BACKEND' | 'FRONTEND' | 'WEBSOCKET' | 'DATABASE',
    level: 'ALL' | 'DEBUG' | 'ERROR' | 'FATAL' | 'INFO' | 'OFF' | 'TRACE' | 'WARN',
    completeLog: string
}