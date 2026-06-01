-- Глобальные переменные
globalX = 100
globalY = 200
globalStr = "global"

-- Объявление глобальных
global a = 1
global b = 2

-- Глобальные константы
global<const> PI = 3.14159
global<const> MAX_SIZE = 1000

-- Локальные переменные
local x = 10
local y = 20.5
local name = "Lua"

-- Локальные константы
local<const> SPEED_OF_LIGHT = 299792458
local<const> DAYS_IN_WEEK = 7

-- Множественное присваивание
local p, q, r = 1, 2, 3
local s, t = 10
local u, v = p, q

-- Обмен значениями
local a, b = 5, 10
a, b = b, a

-- Закрываемая переменная
local<close> file = io.open("test.txt", "r")