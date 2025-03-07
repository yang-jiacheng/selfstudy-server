class MessageSystem {
    static queue = [];
    static offset = 20;

    constructor({ type, message, duration }) {
        this.duration = duration || 3000
        this.el  = this.createMsgElement(type,  message);
        this.show();
        this.autoClose();
    }

    createMsgElement(type, msg) {
        const container = document.createElement('div');
        container.className  = `el-message el-message--${type}`;

        // 图标容器
        const icon = document.createElement('div');
        icon.className  = 'el-message__icon';
        container.appendChild(icon);

        // 文本内容
        const content = document.createElement('div');
        content.className  = 'el-message__content';
        content.textContent  = msg;
        container.appendChild(content);

        return container;
    }

    show() {
        document.body.appendChild(this.el);
        this.calcPosition();
        setTimeout(() => {
            this.el.classList.add('fade-in');
            MessageSystem.queue.push(this);
        }, 50);
    }

    calcPosition() {
        const prevHeight = MessageSystem.queue.reduce((acc,  cur) =>
            acc + cur.el.offsetHeight  + 16, 0
        );
        this.el.style.top  = `${MessageSystem.offset  + prevHeight}px`;
    }

    autoClose() {
        this.timer  = setTimeout(() => {
            this.el.style.opacity  = '0';
            setTimeout(() => this.destroy(), 300)
        }, this.duration);

        // 悬停逻辑优化
        this.el.addEventListener('mouseenter',  () => clearTimeout(this.timer));
        this.el.addEventListener('mouseleave',  () => {
            this.timer  = setTimeout(() => this.destroy(),  this.duration  * 0.5)  // 剩余时间改为总时长一半
        });
    }

    destroy() {
        this.el.remove();
        const index = MessageSystem.queue.indexOf(this);
        if (index > -1) MessageSystem.queue.splice(index,  1);
        this.adjustPositions();
    }

    adjustPositions() {
        MessageSystem.queue.forEach((msg,  index) => {
            const prevHeight = MessageSystem.queue.slice(0,  index)
                .reduce((acc, cur) => acc + cur.el.offsetHeight  + 16, 0);
            msg.el.style.top  = `${MessageSystem.offset  + prevHeight}px`;
        });
    }
}

// API调用示例
const $message = {
    success: (msg, duration) => new MessageSystem({ type: 'success', message: msg, duration }),
    warning: (msg, duration) => new MessageSystem({ type: 'warning', message: msg, duration }),
    error: (msg, duration) => new MessageSystem({ type: 'error', message: msg, duration }),
    info: (msg, duration) => new MessageSystem({ type: 'info', message: msg, duration })
};
