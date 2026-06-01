-- Короткие строки
local a = "Hello, World!"
local b = 'Single quotes'
local c = "Line1\nLine2"
local d = "Tab\there"
local e = "Quote: \" inside"
local f = 'Quote: \' inside'
local g = "Backslash: \\"
local h = "Unicode: 你好"

-- Длинные строки (многострочные)
local i = [[
Это
многострочная
строка
]]

local j = [=[
Строка с [[вложенными]] скобками
]=]

local k = [==[
Уровень 2
]==]

-- Escape последовательности
local l = "Bell: \a"
local m = "Backspace: \b"
local n = "Form feed: \f"
local o = "Carriage return: \r"
local p = "Vertical tab: \v"

-- Строки с нулевыми символами (редко)
local q = "Hello\0World"