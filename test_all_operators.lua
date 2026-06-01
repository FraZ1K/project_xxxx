-- Арифметические
local a = 10 + 5
local b = 10 - 5
local c = 10 * 5
local d = 10 / 5
local e = 10 % 3
local f = 2 ^ 3
local g = -5
local h = -10

-- Сравнения
local eq = (a == b)
local ne = (a ~= b)
local lt = (a < b)
local gt = (a > b)
local le = (a <= b)
local ge = (a >= b)

-- Логические
local andOp = true and false
local orOp = true or false
local notOp = not true

-- Побитовые (Lua 5.3+)
local band = 10 & 7
local bor = 10 | 7
local bxor = 10 ~ 7
local lshift = 10 << 1
local rshift = 10 >> 1

-- Конкатенация
local str = "Hello" .. " " .. "World"

-- Длина
local len = #str