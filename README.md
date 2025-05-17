# eChest

> **Save yourself from theft!**  
> A virtual ender‑chest system for your SMP: create, load, and delete named e‑chests of configurable sizes.

## 📦 Features
- Create multiple virtual ender‑chests by name  
- Chest sizes in multiples of 9 (min: 9, max: 54)  
- Load and delete existing e‑chests on demand  

## 🚀 Installation
1. Download the latest `Echest-1.0.jar` from the [Releases](https://github.com/infenoid/echest-plugin/releases).  
2. Drop it into your server’s `/plugins` folder.  
3. Restart or reload the server (`/reload`).

## ⚙️ Commands

/echest create <name> <size>

Create a new e‑chest  
- `<name>`: unique identifier  
- `<size>`: number of slots (9, 18, 27, 36, 45, 54)

/echest load <name>

Open an existing e‑chest

/echest delete <name>

Remove an existing e‑chest

## 🤝 Donate
UPI - satwikg@fam

## 🔒 Permissions
| Permission                | Description                   | Default |
|---------------------------|-------------------------------|:-------:|
| `echest.use`              | Use `/echest load`            | `op`    |
| `echest.create`           | Run `/echest create`          | `op`    |
| `echest.delete`           | Run `/echest delete`          | `op`    |
