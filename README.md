# eChest

> **Save yourself from theft!**  
> A virtual ender‑chest system for your SMP: create, load, and delete named e‑chests of configurable sizes.

## 📦 Features
- Create multiple virtual ender‑chests by name  
- Chest sizes in multiples of 9 (min: 9, max: 54)  
- Load and delete existing e‑chests on demand  

## 🚀 Installation
1. Download the latest `echest-plugin.jar`.  
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

## 🔒 Permissions
| Permission                | Description                   | Default |
|---------------------------|-------------------------------|:-------:|
| `echest.use`              | Use `/echest load`            | `op`    |
| `echest.create`           | Run `/echest create`          | `op`    |
| `echest.delete`           | Run `/echest delete`          | `op`    |

## 🛠️ Configuration
After first run, edit `plugins/EChest/config.yml`:
```yaml
# max allowed chest size (must be multiple of 9)
max-size: 54
# default chest size when omitted
default-size: 27
