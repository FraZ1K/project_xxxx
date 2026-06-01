-- Простой if
local a = 10
if a > 5 then
    print("a > 5")
end

-- If-else
if a > 15 then
    print("a > 15")
else
    print("a <= 15")
end

-- If-elseif-else
local b = 10
if b < 0 then
    print("negative")
elseif b == 0 then
    print("zero")
else
    print("positive")
end

-- Вложенный if
local x = 10
local y = 20
if x > 0 then
    if y > 0 then
        print("both positive")
    end
end

-- Условия с логическими операторами
local age = 25
local hasLicense = true
if age >= 18 and hasLicense then
    print("Can drive")
end

if age < 18 or age > 65 then
    print("Special rate")
end

if not (age < 18) then
    print("Adult")
end

-- Тернарный оператор (через and/or)
local max = (a > b) and a or b