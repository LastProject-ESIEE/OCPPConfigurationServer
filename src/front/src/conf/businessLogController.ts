import { ChargePoint } from "./chargePointController";

export type BusinessLog = {
    id: number,
    date: Date,
    user: User,
    chargepoint: ChargePoint,
    category: 'LOGIN' | 'STATUS' | 'FIRM' | 'CONFIG',
    level: 'ALL' | 'DEBUG' | 'ERROR' | 'FATAL' | 'INFO' | 'OFF' | 'TRACE' | 'WARN',
    completeLog: string
}

export type User = {
    id: number,
    firstName: string,
    lastName: string,
    email: string,
    role: 'VISUALIZER' | 'EDITOR' | 'ADMINISTRATOR'
}